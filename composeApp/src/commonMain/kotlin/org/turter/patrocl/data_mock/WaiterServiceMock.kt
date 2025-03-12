package org.turter.patrocl.data_mock

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.turter.patrocl.data_mock.utils.WaiterDataSupplier
import org.turter.patrocl.domain.model.BindStatus
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.person.StationWaiterStatus
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.domain.service.WaiterService

class WaiterServiceMock: WaiterService {
    private val DEFAULT_WAITER = WaiterDataSupplier.getWaiter()

    private val waiterFetchState = FetchState.success(DEFAULT_WAITER)

    private val waiterFlow = MutableStateFlow(waiterFetchState)

    private val loggedInWaitersState = FetchState.success(WaiterDataSupplier.getLoggedInWaiters())

    private val loggedInWaitersFlow = MutableStateFlow(loggedInWaitersState)

    private val waiterBindStatus = MutableStateFlow<BindStatus>(BindStatus.Bind)

    private val stationWaiterStatus = MutableStateFlow<StationWaiterStatus>(StationWaiterStatus.LoggedIn)

    override fun getOwnWaiterStateFlow(): StateFlow<FetchState<Waiter>> =
        waiterFlow

    override fun getOwnWaiterBindStatus(): StateFlow<BindStatus> =
        waiterBindStatus.asStateFlow()

    override suspend fun checkWaiter() {
        waiterBindStatus.value = BindStatus.Loading
        delay(300)
        waiterFlow.value = waiterFetchState
    }

    override suspend fun updateWaiterFromRemote(): Result<Waiter> {
        waiterBindStatus.value = BindStatus.Loading
        delay(300)
        waiterFlow.value = waiterFetchState
        return Result.success(DEFAULT_WAITER)
    }

    override fun getStationOwnWaiterStatus(): StateFlow<StationWaiterStatus> {
        return stationWaiterStatus.asStateFlow()
    }

    override suspend fun checkStationWaiterStatus() {
        stationWaiterStatus.value = StationWaiterStatus.Loading
        delay(300)
        stationWaiterStatus.value = StationWaiterStatus.LoggedIn
    }

    override fun getLoggedInWaitersInSameStation(): StateFlow<FetchState<List<Waiter>>> {
        return loggedInWaitersFlow.asStateFlow()
    }

    override suspend fun checkLoggedInWaitersInSameStation() {
        delay(300)
        loggedInWaitersFlow.value = loggedInWaitersState
    }
}