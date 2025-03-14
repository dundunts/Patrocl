package org.turter.patrocl.data.fetcher

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.turter.patrocl.data.dto.source.dataversion.CompanySourceDataVersion
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository
import org.turter.patrocl.data.local.repository.HallLocalRepository
import org.turter.patrocl.data.local.repository.impl.prefs.SourceDataPrefsImpl
import org.turter.patrocl.data.mapper.hall.toHallInfoListFromLocal
import org.turter.patrocl.data.mapper.hall.toHallLocalList
import org.turter.patrocl.data.mapper.version.toCompanySourceDataVersionLocal
import org.turter.patrocl.data.remote.client.HallApiClient
import org.turter.patrocl.domain.fetcher.HallFetcher
import org.turter.patrocl.domain.model.DataStatus
import org.turter.patrocl.domain.model.DataStatus.Empty
import org.turter.patrocl.domain.model.DataStatus.Loading
import org.turter.patrocl.domain.model.DataStatus.Ready
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.enums.SourceDataType
import org.turter.patrocl.domain.model.hall.HallInfo
import org.turter.patrocl.domain.model.hall.HallsData

class HallFetcherImpl(
    private val hallApiClient: HallApiClient,
    private val hallRepository: HallLocalRepository,
    private val dataVersionRepository: CompanySourceDataVersionLocalRepository,
    private val sourceDataPrefs: SourceDataPrefsImpl
//    private val companyInfoRepository: CompanySourcesInfoLocalRepository
) : HallFetcher {
    private val log = Logger.withTag("TableFetcherImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val hallFlow = hallRepository
        .get()
        .map { res -> res.map { it.toHallInfoListFromLocal() } }
        .distinctUntilChanged()

    private val defaultHallRkIdFlow = sourceDataPrefs
        .getDefaultHallRkId()
        .distinctUntilChanged()

    private val refreshTableFlow = MutableSharedFlow<Unit>(replay = 1)

    private val tableDataStatusFlow = MutableStateFlow<DataStatus>(DataStatus.Initial)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tablesStateFlow = flow<FetchState<HallsData>> {
        log.d { "Start tables state flow" }
        refreshTableFlow.emit(Unit)
        refreshTableFlow.collect {
            log.d { "Collecting table refresh event" }
            emit(FetchState.loading())
            tableDataStatusFlow.emit(Loading)

            hallFlow.flatMapLatest { current ->
                log.d { "Current tables result: $current" }
                if (current.isSuccess) {
                    log.d { "Current tables result is success - emit current value" }
                    flowOf(current)
                } else {
                    flow<Result<List<HallInfo>>> {
                        log.d { "Current tables result is failure - start updating from remote " }
                        refreshFromRemote()
                        emitAll(hallFlow)
                    }
                }
            }.combine(defaultHallRkIdFlow) { hallsRes, defaultRkIdRes ->
                try {
                    if (defaultRkIdRes.isBlank()) throw RuntimeException("Default hall rk id is blank")
                    Result.success(
                        HallsData(
                            defaultHallRkId = defaultRkIdRes,
                            halls = hallsRes.getOrThrow()
                        )
                    )
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }.collect { result ->
                result.fold(
                    onSuccess = { value ->
                        emit(FetchState.success(value))
                        log.d { "Tables result is success - emit data status READY" }
                        tableDataStatusFlow.emit(Ready)
                    },
                    onFailure = { cause ->
                        emit(FetchState.fail(cause))
                        log.d { "Current tables not present - emit data status EMPTY" }
                        tableDataStatusFlow.emit(Empty)
                    }
                )
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    override fun getStateFlow(): StateFlow<FetchState<HallsData>> = tablesStateFlow

    override fun getDataStatus(): StateFlow<DataStatus> = tableDataStatusFlow.asStateFlow()

    override fun getActualCount(): Long {
        return hallRepository.count()
    }

    override suspend fun refresh() {
        refreshTableFlow.emit(Unit)
    }

    override suspend fun refreshFromRemote() {
        log.d { "Start updating tables from remote" }
        tableDataStatusFlow.emit(Loading)
        hallApiClient.getHallsForUser().fold(
            onSuccess = { res ->
                val companyId = res.companyId
                val halls = res.halls
                log.d {
                    "Success fetching halls from remote - start replace to local data. " +
                            "Company id: $companyId " +
                            "Halls list size: ${halls.size}"
                }
                hallRepository.replace(halls.toHallLocalList())
                sourceDataPrefs.setDefaultHallRkId(res.defaultHallRkId)
                dataVersionRepository.updateVersion(
                    CompanySourceDataVersion.forHalls(
                        companyId,
                        halls.size.toLong(),
                        res.version
                    ).toCompanySourceDataVersionLocal()
                )
                tableDataStatusFlow.emit(Ready)
            },
            onFailure = { cause ->
                log.e { "Fail fetching tables from remote - start cleanup local data" }
                hallRepository.cleanUp()
                dataVersionRepository.deleteVersionFor(SourceDataType.COMPANY_HALLS)
                tableDataStatusFlow.emit(Empty)
            }
        )
    }
}