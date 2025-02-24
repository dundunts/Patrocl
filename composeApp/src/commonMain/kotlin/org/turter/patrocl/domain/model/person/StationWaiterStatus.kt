package org.turter.patrocl.domain.model.person

sealed class StationWaiterStatus {

    data object Initial: StationWaiterStatus()

    data object Loading: StationWaiterStatus()

    data object LoggedIn: StationWaiterStatus()

    data object NotLoggedIn: StationWaiterStatus()

    data class Error(val causes: List<Throwable>): StationWaiterStatus()

}