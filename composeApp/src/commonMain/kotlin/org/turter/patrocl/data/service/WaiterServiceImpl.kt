package org.turter.patrocl.data.service

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
import org.turter.patrocl.data.local.repository.OwnWaiterLocalRepository
import org.turter.patrocl.data.mapper.person.toWaiter
import org.turter.patrocl.data.mapper.person.toWaiterList
import org.turter.patrocl.data.mapper.person.toWaiterLocal
import org.turter.patrocl.data.remote.client.WaiterApiClient
import org.turter.patrocl.domain.model.BindStatus
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.person.StationWaiterStatus
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.domain.service.WaiterService

class WaiterServiceImpl(
    private val waiterApiClient: WaiterApiClient,
    private val ownWaiterLocalRepository: OwnWaiterLocalRepository
) : WaiterService {
    private val log = Logger.withTag("WaiterServiceImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val waiterFlow = ownWaiterLocalRepository
        .get()
        .map { res -> res.map { it.toWaiter() } }
        .distinctUntilChanged()

    private val refreshWaiterFlow = MutableSharedFlow<Unit>(replay = 1)

    private val waiterDataStatusFlow = MutableStateFlow<BindStatus>(BindStatus.Initial)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val waiterStateFlow = flow<FetchState<Waiter>> {
        log.d { "Start waiter state flow" }
        refreshWaiterFlow.emit(Unit)
        refreshWaiterFlow.collect {
            log.d { "Collecting waiter refresh event" }
            emit(FetchState.loading())
            waiterDataStatusFlow.emit(BindStatus.Loading)

            waiterFlow.flatMapLatest { current ->
                log.d { "Current waiter result: $current" }
                if (current.isSuccess) {
                    log.d { "Current waiter result is success - emit current value" }
                    flowOf(current)
                } else {
                    flow<Result<Waiter>> {
                        log.d { "Current waiter result is failure - start updating from remote " }
                        updateWaiterFromRemote()
                        emitAll(waiterFlow)
                    }
                }
            }.collect { result ->
                result.fold(
                    onSuccess = { value ->
                        emit(FetchState.success(value))
                        log.d { "Waiter result is success - emit bind status BIND" }
                        waiterDataStatusFlow.emit(BindStatus.Bind)
                    },
                    onFailure = { cause ->
                        emit(FetchState.fail(cause))
                        log.d { "Current waiter not present - emit bind status NOT_BIND" }
                        waiterDataStatusFlow.emit(BindStatus.NotBind)
                    }
                )
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    private val refreshLoggedInWaitersFlow = MutableSharedFlow<Unit>(replay = 1)

    private val loggedInWaitersFlow: StateFlow<FetchState<List<Waiter>>> = flow {
        log.d { "Start logged in waiters flow" }
        refreshLoggedInWaitersFlow.emit(Unit)
        refreshLoggedInWaitersFlow.collect {
            log.d { "Collect refresh logged in waiters event - call api" }
            val res = waiterApiClient
                .getLoggedInWaitersInSameStation()
                .map { it.toWaiterList() }
            emit(FetchState.done(res.onSuccess { log.d { "Success calling api, result: $it" } }
                .onFailure { log.e { "Failure calling api, cause: $it" } }))
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    private val stationWaiterStatus: StateFlow<StationWaiterStatus> =
        combine(waiterStateFlow, loggedInWaitersFlow) { ownWaiterFetched, loggedInWaitersFetched ->
            log.d {
                "Combine own waiter flow and logged in waiters flow for station waiter status." +
                        "Fetch states: $ownWaiterFetched, $loggedInWaitersFetched"
            }
            if (ownWaiterFetched.isError() || loggedInWaitersFetched.isError()) {
                log.d { "Error in some flows while getting station waiter status" }
                return@combine StationWaiterStatus.Error(mutableListOf<Throwable>().apply {
                    ownWaiterFetched.takeCauseIfFailure()?.let { add(it) }
                    loggedInWaitersFetched.takeCauseIfFailure()?.let { add(it) }
                })
            }
            val waiter = ownWaiterFetched.takeIfSuccess()
            val loggedInWaiters = loggedInWaitersFetched.takeIfSuccess()
            log.d { "Own waiter: $waiter, logged in waiters: $loggedInWaiters" }
            return@combine if (waiter != null && loggedInWaiters != null) {
                if (loggedInWaiters.any { it.rkId == waiter.rkId }) StationWaiterStatus.LoggedIn
                else StationWaiterStatus.NotLoggedIn
            } else {
                StationWaiterStatus.Loading
            }
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = StationWaiterStatus.Initial
        )


    override fun getOwnWaiterStateFlow(): StateFlow<FetchState<Waiter>> =
        waiterStateFlow

    override fun getOwnWaiterBindStatus(): StateFlow<BindStatus> =
        waiterDataStatusFlow.asStateFlow()

    override suspend fun checkWaiter() {
        log.d { "Check waiter" }
        refreshWaiterFlow.emit(Unit)
    }

    override suspend fun updateWaiterFromRemote(): Result<Waiter> {
        log.d { "Start updating waiter from remote" }
        waiterApiClient.getOwnWaiterForUser().fold(
            onSuccess = { waiterDto ->
                log.d {
                    "Success fetching waiter from remote - start replace to local data. " +
                            "WaiterDto: $waiterDto"
                }
                ownWaiterLocalRepository.replace(waiterDto.toWaiterLocal())
                return Result.success(waiterDto.toWaiter())
            },
            onFailure = { cause ->
                log.e { "Fail fetching waiter from remote - start cleanup local data" }
                ownWaiterLocalRepository.cleanUp()
                return Result.failure(cause)
            }
        )
    }

    override fun getStationOwnWaiterStatus(): StateFlow<StationWaiterStatus> = stationWaiterStatus

    override suspend fun checkStationWaiterStatus() {
        log.d { "Check station waiter status" }
        refreshWaiterFlow.emit(Unit)
        refreshLoggedInWaitersFlow.emit(Unit)
    }

    override fun getLoggedInWaitersInSameStation(): StateFlow<FetchState<List<Waiter>>> =
        loggedInWaitersFlow

    override suspend fun checkLoggedInWaitersInSameStation() {
        log.d { "Check logged in waiters" }
        refreshLoggedInWaitersFlow.emit(Unit)
    }
}