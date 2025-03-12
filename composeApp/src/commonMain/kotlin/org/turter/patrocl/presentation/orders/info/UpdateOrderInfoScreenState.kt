package org.turter.patrocl.presentation.orders.info

import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.presentation.orders.common.OrderInfo

data class UpdateOrderInfoScreenState(
    val info: OrderInfo,
    val halls: HallsData,
    val availableWaiters: List<Waiter>,
    val isTablePickerOpened: Boolean,
    val isWaiterPickerOpened: Boolean = false,
    val isLoading: Boolean = false
) {
}