package org.turter.patrocl.data.fetcher

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.turter.patrocl.data.dto.enums.SourceDataType
import org.turter.patrocl.data.dto.source.dataversion.CompanySourceDataVersion
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository
import org.turter.patrocl.data.local.repository.OrderItemVoidLocalRepository
import org.turter.patrocl.data.mapper.version.toCompanySourceDataVersionLocal
import org.turter.patrocl.data.mapper.voids.toOrderItemVoidInfoListFromLocal
import org.turter.patrocl.data.mapper.voids.toOrderItemVoidLocalList
import org.turter.patrocl.data.remote.client.OrderItemVoidApiClient
import org.turter.patrocl.domain.exception.EmptyMenuDataModifiersException
import org.turter.patrocl.domain.fetcher.OrderItemVoidFetcher
import org.turter.patrocl.domain.model.DataStatus
import org.turter.patrocl.domain.model.DataStatus.Empty
import org.turter.patrocl.domain.model.DataStatus.Initial
import org.turter.patrocl.domain.model.DataStatus.Loading
import org.turter.patrocl.domain.model.DataStatus.Ready
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.voids.OrderItemVoidInfo

class OrderItemVoidFetcherImpl(
    private val voidsClient: OrderItemVoidApiClient,
    private val voidsRepository: OrderItemVoidLocalRepository,
    private val dataVersionRepository: CompanySourceDataVersionLocalRepository
) : OrderItemVoidFetcher {
    private val log = Logger.withTag("OrderItemVoidFetcherImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val voidsFlow = voidsRepository
        .get()
        .map { res ->
            res.map { it.toOrderItemVoidInfoListFromLocal() }
        }
        .distinctUntilChanged()

    private val refreshVoidsFlow = MutableSharedFlow<Unit>(replay = 1)

    private val voidsDataStatus = MutableStateFlow<DataStatus>(Initial)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val voidsStateFlow = flow<FetchState<List<OrderItemVoidInfo>>> {
        log.d { "Creating order items voids state flow" }
        refreshVoidsFlow.emit(Unit)
        refreshVoidsFlow.collect {
            log.d { "Order items voids state flow - collect event" }
            emit(FetchState.loading())
            voidsDataStatus.emit(Loading)

            voidsFlow.flatMapLatest { current ->
                log.d { "Order items voids state flow - latest voids list result: $current" }
                if (current.isSuccess) {
                    log.d { "Order items voids is present - emit current value" }
                    flowOf(current)
                } else {
                    flow<Result<List<OrderItemVoidInfo>>> {
                        log.d { "Order items voids result is failure - start updating from remote" }
                        refreshFromRemote()
                        emitAll(voidsFlow)
                    }
                }
            }.collect { result ->
                result.fold(
                    onSuccess = {
                        emit(FetchState.done(result))
                        log.d { "Current order items voids is present, emit data status READY" }
                        voidsDataStatus.emit(Ready)
                    },
                    onFailure = {
                        emit(FetchState.fail(EmptyMenuDataModifiersException()))
                        log.d { "Current order items voids is empty, emit data status EMPTY" }
                        voidsDataStatus.emit(Empty)
                    }
                )
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    override fun getStateFlow(): StateFlow<FetchState<List<OrderItemVoidInfo>>> =
        voidsStateFlow

    override fun getDataStatus(): StateFlow<DataStatus> = voidsDataStatus.asStateFlow()

    override suspend fun refresh() {
        refreshVoidsFlow.emit(Unit)
    }

    override suspend fun refreshFromRemote() {
        log.d { "Start updating order items voids from remote" }
        voidsDataStatus.emit(Loading)
        voidsClient.getOrderItemVoidsForUser().fold(
            onSuccess = { res ->
                val voids = res.voids
                log.d {
                    "Success fetching order items voids from remote - start replace to local data. " +
                            "Order items voids list size: ${voids.size}"
                }
                voidsRepository.replace(voids.toOrderItemVoidLocalList())
                dataVersionRepository.updateVersion(
                    CompanySourceDataVersion.forOrderItemVoids(
                        res.companyId,
                        voids.size.toLong(),
                        res.version
                    ).toCompanySourceDataVersionLocal()
                )
                voidsDataStatus.emit(Ready)
            },
            onFailure = { cause ->
                log.e { "Fail fetching order items voids from remote - start cleanup local data" }
                voidsRepository.cleanUp()
                dataVersionRepository.deleteVersionFor(SourceDataType.COMPANY_ORDER_ITEM_VOIDS)
                voidsDataStatus.emit(Empty)
            }
        )
    }
}