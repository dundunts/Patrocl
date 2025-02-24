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
import org.turter.patrocl.data.dto.source.dataversion.CompanySourceDataVersion
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository
import org.turter.patrocl.data.local.repository.DishLocalRepository
import org.turter.patrocl.data.mapper.menu.toDishLocalList
import org.turter.patrocl.data.mapper.menu.toStationDishInfoList
import org.turter.patrocl.data.mapper.version.toCompanySourceDataVersionLocal
import org.turter.patrocl.data.remote.client.MenuApiClient
import org.turter.patrocl.domain.exception.EmptyMenuDataDishesException
import org.turter.patrocl.domain.fetcher.DishFetcher
import org.turter.patrocl.domain.model.DataStatus
import org.turter.patrocl.domain.model.DataStatus.Empty
import org.turter.patrocl.domain.model.DataStatus.Initial
import org.turter.patrocl.domain.model.DataStatus.Loading
import org.turter.patrocl.domain.model.DataStatus.Ready
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.menu.StationDishInfo

class DishFetcherImpl(
    private val menuApiClient: MenuApiClient,
    private val dishRepository: DishLocalRepository,
    private val dataVersionRepository: CompanySourceDataVersionLocalRepository
) : DishFetcher {
    private val log = Logger.withTag("DishFetcherImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val dishesFlow = dishRepository
        .get()
        .map { res ->
            res.map { it.toStationDishInfoList() }
        }
        .distinctUntilChanged()

    private val refreshDishesFlow = MutableSharedFlow<Unit>(replay = 1)

    private val dishDataStatus = MutableStateFlow<DataStatus>(Initial)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dishesStateFlow = flow<FetchState<List<StationDishInfo>>> {
        log.d { "Creating dishes state flow" }
        refreshDishesFlow.emit(Unit)
        refreshDishesFlow.collect {
            log.d { "Dishes state flow - collect event" }
            emit(FetchState.loading())
            dishDataStatus.emit(Loading)

            dishesFlow.flatMapLatest { current ->
                log.d { "Dishes state flow - latest dish list result: $current" }
                if (current.isSuccess) {
                    log.d { "Dishes is present - emit current value" }
                    flowOf(current)
                } else {
                    flow<Result<List<StationDishInfo>>> {
                        log.d { "Dishes result is failure - start updating from remote" }
                        refreshFromRemote()
                        emitAll(dishesFlow)
                    }
                }
            }.collect { result ->
                result.fold(
                    onSuccess = {
                        emit(FetchState.done(result))
                        log.d { "Current dishes is present, emit data status READY" }
                        dishDataStatus.emit(Ready)
                    },
                    onFailure = {
                        emit(FetchState.fail(EmptyMenuDataDishesException()))
                        log.d { "Current dishes is empty, emit data status EMPTY" }
                        dishDataStatus.emit(Empty)
                    }
                )
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    override fun getStateFlow(): StateFlow<FetchState<List<StationDishInfo>>> = dishesStateFlow

    override fun getDataStatus(): StateFlow<DataStatus> = dishDataStatus.asStateFlow()

    override suspend fun refresh() {
        refreshDishesFlow.emit(Unit)
    }

    override suspend fun refreshFromRemote() {
        log.d { "Start updating dishes from remote" }
        dishDataStatus.emit(Loading)
        menuApiClient.getAvailableStationDishesForUser().fold(
            onSuccess = { res ->
                val dishes = res.dishes
                log.d {
                    "Success fetching dishes from remote - start replace to local data. " +
                            "Dish list size: ${dishes.size}"
                }
                dishRepository.replace(dishes.toDishLocalList())
                dataVersionRepository.updateVersion(
                    CompanySourceDataVersion.forDishes(
                        res.companyId,
                        dishes.size.toLong(),
                        res.version
                    ).toCompanySourceDataVersionLocal()
                )
                dishDataStatus.emit(Ready)
            },
            onFailure = { cause ->
                log.e { "Fail fetching dishes from remote - start cleanup local data" }
                dishRepository.cleanUp()
                dishDataStatus.emit(Empty)
            }
        )
    }
}