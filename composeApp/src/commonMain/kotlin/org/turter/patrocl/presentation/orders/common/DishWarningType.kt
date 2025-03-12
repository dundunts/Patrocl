package org.turter.patrocl.presentation.orders.common

sealed class DishWarningType {
    data object None : DishWarningType()
    data object OnStop : DishWarningType()
    data object LowRemain : DishWarningType()

    companion object {
        fun of(onStop: Boolean, remainCount: Int) = if (onStop) OnStop
        else if (remainCount < 5) LowRemain
        else None
    }
}