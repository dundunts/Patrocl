package org.turter.patrocl.domain.service

import kotlinx.coroutines.flow.StateFlow
import org.turter.patrocl.domain.model.BindStatus
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.person.StationWaiterStatus
import org.turter.patrocl.domain.model.person.Waiter

interface WaiterService {
    fun getOwnWaiterStateFlow(): StateFlow<FetchState<Waiter>>
    fun getOwnWaiterBindStatus(): StateFlow<BindStatus>
    suspend fun checkWaiter()
    suspend fun updateWaiterFromRemote(): Result<Waiter>

    fun getStationOwnWaiterStatus(): StateFlow<StationWaiterStatus>
    suspend fun checkStationWaiterStatus()

    fun getLoggedInWaitersInSameStation(): StateFlow<FetchState<List<Waiter>>>
    suspend fun checkLoggedInWaitersInSameStation()
}