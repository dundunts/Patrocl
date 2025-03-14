package org.turter.patrocl.presentation.orders.common

import org.turter.patrocl.domain.model.hall.TableInfo
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.person.Waiter

data class OrderInfo(
    val waiter: Waiter,
    val table: TableInfo?
) {
}