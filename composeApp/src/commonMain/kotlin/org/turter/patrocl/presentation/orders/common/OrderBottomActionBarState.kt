package org.turter.patrocl.presentation.orders.common

sealed class OrderBottomActionBarState {

    data object Default: OrderBottomActionBarState()

    data object SelectedNewItem: OrderBottomActionBarState()

}