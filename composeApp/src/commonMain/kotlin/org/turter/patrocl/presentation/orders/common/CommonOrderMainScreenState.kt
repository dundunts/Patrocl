package org.turter.patrocl.presentation.orders.common

import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.presentation.orders.common.interceptor.InterceptedAddingDish

interface CommonOrderMainScreenState <T> {
    val menu: MenuTreeData
    val halls: HallsData
    val ownWaiter: Waiter
    val waiters: List<Waiter>
    val orderInfo: OrderInfo
    val selected: Selected
    val newOrderItems: List<NewOrderItem>
    val interceptedAdding: InterceptedAddingDish?
    val isSaving: Boolean
    val customCommentInputState: CustomCommentInputState
    val quantityInputState: QuantityInputState
    val menuState: MenuState
    val isBottomSheetOpened: Boolean

    fun copyCommon(
        menu: MenuTreeData = this.menu,
        halls: HallsData = this.halls,
        ownWaiter: Waiter = this.ownWaiter,
        waiters: List<Waiter> = this.waiters,
        orderInfo: OrderInfo = this.orderInfo,
        selected: Selected = this.selected,
        newOrderItems: List<NewOrderItem> = this.newOrderItems,
        interceptedAdding: InterceptedAddingDish? = this.interceptedAdding,
        isSaving: Boolean = this.isSaving,
        customCommentInputState: CustomCommentInputState = this.customCommentInputState,
        quantityInputState: QuantityInputState = this.quantityInputState,
        menuState: MenuState = this.menuState,
        isBottomSheetOpened: Boolean = this.isBottomSheetOpened
    ): T
}