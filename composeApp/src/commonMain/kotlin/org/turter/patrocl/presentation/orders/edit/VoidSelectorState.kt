package org.turter.patrocl.presentation.orders.edit

import org.turter.patrocl.domain.model.order.Order

sealed class VoidSelectorState {

    data object Closed : VoidSelectorState()

    data object ForSelectedItems : VoidSelectorState()

    data class ForSessionItem(val session: Order.Session, val item: Order.Dish) :
        VoidSelectorState()

}