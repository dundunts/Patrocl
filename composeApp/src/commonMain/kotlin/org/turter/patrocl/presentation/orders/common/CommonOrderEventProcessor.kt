package org.turter.patrocl.presentation.orders.common

import co.touchlab.kermit.Logger
import com.benasher44.uuid.Uuid
import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.menu.CategoryInfo
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.presentation.orders.common.interceptor.AddingWarningType
import org.turter.patrocl.presentation.orders.common.interceptor.InterceptedAddingDish
import org.turter.patrocl.utils.mutate

class CommonOrderEventProcessor<T : CommonOrderMainScreenState<T>>(
    private val transformMainState: ((T) -> T) -> Unit,
    private val withMainState: () -> T?,
    private val navigateToModifiersSelector: (
        item: NewOrderItem,
        menuData: MenuTreeData,
        autoOpened: Boolean,
        onSave: (item: NewOrderItem) -> Unit
    ) -> Unit,
    private val navigateToUpdateOrderInfoScreen: (
        info: OrderInfo,
        halls: HallsData,
        availableWaiters: List<Waiter>,
        onSave: (info: OrderInfo) -> Result<Unit>
    ) -> Unit,
    private val onUpdateOrderInfo: (info: OrderInfo) -> Result<Unit>,
    private val navigateBack: () -> Unit
) {
    private val log = Logger.withTag("CommonOrderEventProcessor")

    fun processEvent(event: CommonOrderUiEvent) {
        when (event) {
            is CommonOrderUiEvent.AddNewOrderItem -> addNewOrderItem(
                dishInfo = event.dishInfo,
                quantity = event.quantity,
                modifiers = event.modifiers
            )

            is CommonOrderUiEvent.AddNewOrderItemByRkId -> addNewOrderItem(
                dishRkId = event.dishRkId,
                quantity = event.quantity,
                modifiers = event.modifiers
            )

            is CommonOrderUiEvent.ConfirmInterceptedAdding -> confirmInterceptedAdding()
            is CommonOrderUiEvent.RejectInterceptedAdding -> cleanInterceptedAdding()

            is CommonOrderUiEvent.RemoveNewOrderItem -> removeNewOrderItem(event.uuid)
            is CommonOrderUiEvent.IncreaseNewOrderItemQuantity -> increaseQuantity(event.item)
            is CommonOrderUiEvent.CreateOrUpdateNewOrderItem -> createOrUpdateOrderItem(event.item)
            is CommonOrderUiEvent.SelectNewOrderItem -> selectNewItem(event.uuid)
            is CommonOrderUiEvent.UnselectNewOrderItem -> unselectNewItem()
            is CommonOrderUiEvent.MoveSelectedItemUp -> moveSelectedItemUp()
            is CommonOrderUiEvent.MoveSelectedItemDown -> moveSelectedItemDown()

            is CommonOrderUiEvent.OpenCommentInput -> openCommentInputFor(event.uuid)
            is CommonOrderUiEvent.CloseCommentInput -> closeCommentInput()
            is CommonOrderUiEvent.SaveComment -> saveComment(content = event.content)

            is CommonOrderUiEvent.OpenQuantityInput -> openQuantityInputFor(event.uuid)
            is CommonOrderUiEvent.CloseQuantityInput -> closeQuantityInput()
            is CommonOrderUiEvent.SetQuantity -> setQuantity(value = event.rqQuantity)

            is CommonOrderUiEvent.OpenMenu -> setMenuTargetStatus(MenuStatus.Opened)
            is CommonOrderUiEvent.CloseMenu -> setMenuTargetStatus(MenuStatus.Closed)
            is CommonOrderUiEvent.SwitchMenu -> switchMenu()
            is CommonOrderUiEvent.OpenDishCategory -> openDishCategory(event.dishRkId)
            is CommonOrderUiEvent.SetCurrentCategory -> setCurrentCategory(event.category)
            is CommonOrderUiEvent.SetMenuCurrentStatus -> setMenuCurrentStatus(event.status)

            is CommonOrderUiEvent.OpenModifiersSelector -> openModifiersSelector(event.item)

            is CommonOrderUiEvent.OpenUpdateOrderInfo -> openUpdateOrderInfo()

            is CommonOrderUiEvent.OpenBottomSheetMenu -> setOpenedBottomSheetMenu(true)
            is CommonOrderUiEvent.HideBottomSheetMenu -> setOpenedBottomSheetMenu(false)

            is CommonOrderUiEvent.NavigateBack -> navigateBack()
        }
    }

    private fun openDishCategory(dishRkId: String) {
        log.d { "Start opening dish category" }
        withMainState()?.apply {
            log.d { "Opening dish category - with menu state" }
            menu.dishRkIdMap[dishRkId]?.let { dish ->
                log.d { "Opening dish category - dish: $dish" }
                menu.categoryRkIdMap[dish.mainParentIdent]?.let { category ->
                    log.d { "Opening dish category - category: $category" }
//                    if (menuState.currentCategory?.rkId != category.rkId)
                    transformMainState {
                        log.d { "Opening dish category - transformMainState" }
                        it.copyCommon(
                            menuState = it.menuState.copy(
                                targetStatus = MenuStatus.Opened,
                                currentCategory = category
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setCurrentCategory(category: CategoryInfo) {
        transformMainState {
            it.copyCommon(menuState = it.menuState.copy(currentCategory = category))
        }
    }

    private fun setOpenedBottomSheetMenu(value: Boolean) {
        transformMainState { it.copyCommon(isBottomSheetOpened = value) }
    }

    private fun switchMenu() {
        withMainState()?.menuState?.apply {
            setMenuTargetStatus(
                if (targetStatus is MenuStatus.Closed) MenuStatus.Opened else MenuStatus.Closed
            )
        }
    }

    private fun setMenuCurrentStatus(status: MenuStatus) {
        transformMainState { it.copyCommon(menuState = it.menuState.copy(currentStatus = status)) }
    }

    private fun setMenuTargetStatus(status: MenuStatus) {
        transformMainState { it.copyCommon(menuState = it.menuState.copy(targetStatus = status)) }
    }

    private fun openUpdateOrderInfo() {
        withMainState()?.apply {
            navigateToUpdateOrderInfoScreen(orderInfo, halls, waiters, onUpdateOrderInfo)
        }
    }

    private fun openModifiersSelector(item: NewOrderItem) {
        withMainState()?.apply {
            navigateToModifiersSelector(item, menu, false) { createOrUpdateOrderItem(it) }
        }
    }

    private fun setQuantity(value: Int) {
        withMainState()?.quantityInputState?.let { quantityInputState ->
            if (quantityInputState is QuantityInputState.Opened) {
                transformMainState { state ->
                    state.copyCommon(
                        newOrderItems = state.newOrderItems.mutate {
                            find { it.uuid == quantityInputState.uuid }?.let { item ->
                                this[indexOf(item)] = item.copy(rkQuantity = value)
                            }
                        },
                        quantityInputState = QuantityInputState.Closed
                    )
                }
            }
        }
    }

    private fun closeQuantityInput() {
        transformMainState { it.copyCommon(quantityInputState = QuantityInputState.Closed) }
    }

    private fun openQuantityInputFor(uuid: Uuid) {
        transformMainState { it.copyCommon(quantityInputState = QuantityInputState.Opened(uuid)) }
    }

    private fun saveComment(content: String) {
        withMainState()?.customCommentInputState?.let { commentState ->
            if (commentState is CustomCommentInputState.Opened) {
                transformMainState { state ->
                    state.copyCommon(
                        newOrderItems = state.newOrderItems.mutate {
                            find { it.uuid == commentState.uuid }?.let { item ->
                                this[indexOf(item)] = item.copy(
                                    modifiers = item.modifiers.toMutableList()
                                        .apply { add(NewOrderItem.Modifier.customComment(content)) }
                                        .toList()
                                )
                            }
                        },
                        customCommentInputState = CustomCommentInputState.Closed
                    )
                }
            }
        }
    }

    private fun closeCommentInput() {
        transformMainState { it.copyCommon(customCommentInputState = CustomCommentInputState.Closed) }
    }

    private fun openCommentInputFor(uuid: Uuid) {
        transformMainState {
            it.copyCommon(
                customCommentInputState = CustomCommentInputState.Opened(
                    uuid
                )
            )
        }
    }

    private fun addNewOrderItem(
        dishInfo: StationDishInfo,
        quantity: Int = 1000,
        modifiers: List<NewOrderItem.Modifier>
    ) {
        withMainState()?.apply {
            this.addNewItem(dishInfo, quantity, modifiers)
        }
    }

    private fun addNewOrderItem(
        dishRkId: String,
        quantity: Int = 1000,
        modifiers: List<NewOrderItem.Modifier>
    ) {
        withMainState()?.apply {
            menu.dishRkIdMap[dishRkId]?.let { this.addNewItem(it, quantity, modifiers) }
        }
    }

    private fun T.addNewItem(
        dishInfo: StationDishInfo,
        quantity: Int,
        modifiers: List<NewOrderItem.Modifier>
    ) {
        val dishId = dishInfo.rkId
        if (menu.dishRkIdMap.containsKey(dishId)) {
            val target = NewOrderItem(
                dishInfo = dishInfo,
                rkQuantity = quantity,
                modifiers = modifiers
            )
            menu.stopListDishRkIdMap[dishId]
                ?.takeIf { it.onStop || it.remainingCount - quantity < 5 }
                ?.let { dish ->
                    transformMainState {
                        it.copyCommon(
                            interceptedAdding = InterceptedAddingDish(
                                target = target,
                                warningType = AddingWarningType.of(
                                    dish.onStop,
                                    dish.remainingCount
                                )
                            )
                        )
                    }
                }
                ?: transformNewOrderItems { add(target) }.also {
                    menu.modifiersSchemeRkIdMap[target.dishInfo.modiScheme]?.autoOpen
                        ?.let { autoOpen ->
                            if (autoOpen) navigateToModifiersSelector(target, menu, autoOpen) {
                                createOrUpdateOrderItem(
                                    it
                                )
                            }
                        }
                }
        }
    }

    private fun confirmInterceptedAdding() {
        withMainState()?.apply {
            interceptedAdding?.let { intercepted ->
                val target = intercepted.target
                transformMainState {
                    it.copyCommon(
                        newOrderItems = it.newOrderItems.mutate { add(target) },
                        interceptedAdding = null
                    )
                }.also {
                    menu.modifiersSchemeRkIdMap[target.dishInfo.modiScheme]?.autoOpen
                        ?.let { autoOpen ->
                            if (autoOpen) navigateToModifiersSelector(target, menu, autoOpen) {
                                createOrUpdateOrderItem(
                                    it
                                )
                            }
                        }
                }
            }
        }
    }

    private fun cleanInterceptedAdding() =
        transformMainState { it.copyCommon(interceptedAdding = null) }

    private fun removeNewOrderItem(uuid: Uuid) {
        withMainState()?.newOrderItems?.find { it.uuid == uuid }?.let { item ->
            transformMainState { state ->
                val newOrderItems = state.newOrderItems.mutate { remove(item) }
                val selected = state.selected
                if (selected is Selected.NewItem && selected.newItemUuid == uuid) {
                    state.copyCommon(
                        newOrderItems = newOrderItems,
                        selected = Selected.None
                    )
                } else {
                    state.copyCommon(newOrderItems = newOrderItems)
                }
            }
        }
    }

    private fun increaseQuantity(orderItem: NewOrderItem) {
        transformNewOrderItems {
            val index = indexOf(orderItem)
            val item = orderItem.copy(rkQuantity = orderItem.rkQuantity + 1000)
            this[index] = item
            log.d { "Index of item to increase: $index, item: $orderItem" }
        }
    }

    private fun createOrUpdateOrderItem(orderItem: NewOrderItem) {
        transformNewOrderItems {
            find { it.uuid == orderItem.uuid }
                ?.let { item -> this[indexOf(item)] = orderItem }
                ?: add(orderItem)
        }
    }

    private fun selectNewItem(uuid: Uuid) = transformMainState {
        it.copyCommon(selected = Selected.NewItem(uuid))
    }

    private fun unselectNewItem() = transformMainState {
        it.copyCommon(selected = Selected.None)
    }

    private fun moveSelectedItemUp() {
        withMainState()?.let { state ->
            state.getSelectedNewItem()?.let { item ->
                transformNewOrderItems {
                    val index = indexOf(item)
                    if (index > 0) {
                        val targetIndex = index - 1
                        val temp = this[targetIndex]
                        this[targetIndex] = item
                        this[index] = temp
                    }
                }
            }
        }
    }

    private fun moveSelectedItemDown() {
        withMainState()?.let { state ->
            state.getSelectedNewItem()?.let { item ->
                transformNewOrderItems {
                    val index = indexOf(item)
                    if (index < size - 1) {
                        val targetIndex = index + 1
                        val temp = this[targetIndex]
                        this[targetIndex] = item
                        this[index] = temp
                    }
                }
            }
        }
    }

    private fun setLoading(value: Boolean) {
        log.d { "Set isSaving to $value" }
        transformMainState { it.copyCommon(isSaving = value) }
    }

    private fun transformNewOrderItems(transform: MutableList<NewOrderItem>.() -> Unit) {
        transformMainState {
            it.copyCommon(newOrderItems = it.newOrderItems.mutate(transform))
        }
    }

}

fun <T : CommonOrderMainScreenState<T>> T.getSelectedNewItem(): NewOrderItem? {
    return when (val currentSelected = selected) {
        is Selected.NewItem -> newOrderItems.find { it.uuid == currentSelected.newItemUuid }
        else -> null
    }
}