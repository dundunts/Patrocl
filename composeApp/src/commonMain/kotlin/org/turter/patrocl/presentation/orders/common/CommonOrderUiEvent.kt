package org.turter.patrocl.presentation.orders.common

import com.benasher44.uuid.Uuid
import org.turter.patrocl.domain.model.menu.CategoryInfo
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.order.NewOrderItem

sealed class CommonOrderUiEvent {
    data class AddNewOrderItem(
        val dishInfo: StationDishInfo,
        val quantity: Int = 1000,
        val modifiers: List<NewOrderItem.Modifier> = emptyList()
    ) : CommonOrderUiEvent()

    data class AddNewOrderItemByRkId(
        val dishRkId: String,
        val quantity: Int = 1000,
        val modifiers: List<NewOrderItem.Modifier> = emptyList()
    ) : CommonOrderUiEvent()

    data object ConfirmInterceptedAdding : CommonOrderUiEvent()
    data object RejectInterceptedAdding : CommonOrderUiEvent()

    data class RemoveNewOrderItem(val uuid: Uuid) : CommonOrderUiEvent()
    data class IncreaseNewOrderItemQuantity(val item: NewOrderItem) : CommonOrderUiEvent()
    data class CreateOrUpdateNewOrderItem(val item: NewOrderItem) : CommonOrderUiEvent()

    data class SelectNewOrderItem(val uuid: Uuid) : CommonOrderUiEvent()
    data object UnselectNewOrderItem : CommonOrderUiEvent()

    data object MoveSelectedItemUp : CommonOrderUiEvent()
    data object MoveSelectedItemDown : CommonOrderUiEvent()

    data class OpenCommentInput(val uuid: Uuid) : CommonOrderUiEvent()
    data object CloseCommentInput : CommonOrderUiEvent()
    data class SaveComment(val content: String) : CommonOrderUiEvent()

    data class OpenQuantityInput(val uuid: Uuid) : CommonOrderUiEvent()
    data object CloseQuantityInput : CommonOrderUiEvent()
    data class SetQuantity(val rqQuantity: Int) : CommonOrderUiEvent()

    data object OpenMenu : CommonOrderUiEvent()
    data object CloseMenu : CommonOrderUiEvent()
    data object SwitchMenu : CommonOrderUiEvent()
    data class OpenDishCategory(val dishRkId: String) : CommonOrderUiEvent()
    data class SetCurrentCategory(val category: CategoryInfo) : CommonOrderUiEvent()
    data class SetMenuCurrentStatus(val status: MenuStatus) : CommonOrderUiEvent()

    data class OpenModifiersSelector(val item: NewOrderItem) : CommonOrderUiEvent()

    data object OpenUpdateOrderInfo : CommonOrderUiEvent()

    data object OpenBottomSheetMenu : CommonOrderUiEvent()
    data object HideBottomSheetMenu : CommonOrderUiEvent()

    data object NavigateBack : CommonOrderUiEvent()
}