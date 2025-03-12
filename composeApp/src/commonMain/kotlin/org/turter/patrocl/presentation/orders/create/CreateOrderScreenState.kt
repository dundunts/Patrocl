package org.turter.patrocl.presentation.orders.create

import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.presentation.error.ErrorType
import org.turter.patrocl.presentation.orders.common.CommonOrderMainScreenState
import org.turter.patrocl.presentation.orders.common.CustomCommentInputState
import org.turter.patrocl.presentation.orders.common.MenuState
import org.turter.patrocl.presentation.orders.common.MenuStatus
import org.turter.patrocl.presentation.orders.common.OrderInfo
import org.turter.patrocl.presentation.orders.common.QuantityInputState
import org.turter.patrocl.presentation.orders.common.interceptor.InterceptedAddingDish
import org.turter.patrocl.presentation.orders.common.Selected

sealed class CreateOrderScreenState {
    data object Initial : CreateOrderScreenState()

    data object Loading : CreateOrderScreenState()

    data class Main(
        override val menu: MenuTreeData,
        override val halls: HallsData,
        override val ownWaiter: Waiter,
        override val waiters: List<Waiter>,
        override val orderInfo: OrderInfo,

        override val newOrderItems: List<NewOrderItem> = listOf(),
        override val interceptedAdding: InterceptedAddingDish? = null,

        override val selected: Selected = Selected.None,

        override val isSaving: Boolean = false,

        override val customCommentInputState: CustomCommentInputState = CustomCommentInputState.Closed,

        override val quantityInputState: QuantityInputState = QuantityInputState.Closed,

        override val menuState: MenuState = MenuState(
            MenuStatus.Closed,
            MenuStatus.Closed,
            menu.categoryRkIdMap[menu.rootCategoryRkId]
        ),

        override val isBottomSheetOpened: Boolean = false
    ) : CreateOrderScreenState(), CommonOrderMainScreenState<Main> {
        override fun copyCommon(
            menu: MenuTreeData,
            halls: HallsData,
            ownWaiter: Waiter,
            waiters: List<Waiter>,
            orderInfo: OrderInfo,
            selected: Selected,
            newOrderItems: List<NewOrderItem>,
            interceptedAdding: InterceptedAddingDish?,
            isSaving: Boolean,
            customCommentInputState: CustomCommentInputState,
            quantityInputState: QuantityInputState,
            menuState: MenuState,
            isBottomSheetOpened: Boolean
        ): Main = copy(
            menu = menu,
            halls = halls,
            ownWaiter = ownWaiter,
            waiters = waiters,
            orderInfo = orderInfo,
            selected = selected,
            newOrderItems = newOrderItems,
            interceptedAdding = interceptedAdding,
            isSaving = isSaving,
            customCommentInputState = customCommentInputState,
            quantityInputState = quantityInputState,
            menuState = menuState,
            isBottomSheetOpened = isBottomSheetOpened
        )
    }

    data class Error(val errorType: ErrorType) : CreateOrderScreenState()
}

