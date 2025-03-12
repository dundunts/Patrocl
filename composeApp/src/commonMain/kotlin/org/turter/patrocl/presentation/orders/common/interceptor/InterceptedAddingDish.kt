package org.turter.patrocl.presentation.orders.common.interceptor

import org.turter.patrocl.domain.model.order.NewOrderItem

data class InterceptedAddingDish(
    val target: NewOrderItem,
    val warningType: AddingWarningType
)

sealed class AddingWarningType {
    data object OnStop : AddingWarningType()
    data class LowRemain(val count: Int) : AddingWarningType()

    companion object {
        fun of(onStop: Boolean, remainCount: Int) = if (onStop) OnStop else LowRemain(remainCount)
    }
}