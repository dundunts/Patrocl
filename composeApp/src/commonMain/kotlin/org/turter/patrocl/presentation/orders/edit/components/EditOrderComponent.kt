package org.turter.patrocl.presentation.orders.edit.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.presentation.components.FullscreenLoader
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent.AddNewOrderItemByRkId
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent.IncreaseNewOrderItemQuantity
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent.OpenCommentInput
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent.OpenDishCategory
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent.OpenModifiersSelector
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent.OpenQuantityInput
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent.RemoveNewOrderItem
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent.SelectNewOrderItem
import org.turter.patrocl.presentation.orders.common.CustomCommentInputState
import org.turter.patrocl.presentation.orders.common.MenuStatus
import org.turter.patrocl.presentation.orders.common.QuantityInputState
import org.turter.patrocl.presentation.orders.common.Selected
import org.turter.patrocl.presentation.orders.common.components.BottomSheetMenu
import org.turter.patrocl.presentation.orders.common.components.CreateCommentDialog
import org.turter.patrocl.presentation.orders.common.components.MenuOption
import org.turter.patrocl.presentation.orders.common.components.RestaurantMenuSelector
import org.turter.patrocl.presentation.orders.common.components.RkQuantityInputDialog
import org.turter.patrocl.presentation.orders.common.components.newItems
import org.turter.patrocl.presentation.orders.common.components.savedItems
import org.turter.patrocl.presentation.orders.common.getSelectedNewItem
import org.turter.patrocl.presentation.orders.common.interceptor.InterceptedAddingDialog
import org.turter.patrocl.presentation.orders.create.components.OrderBottomActionBar
import org.turter.patrocl.presentation.orders.edit.EditOrderScreenState
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.OpenVoidSelectorForSessionItem
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.SelectAllSavedSessionItems
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.SelectSavedItem
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.UnselectAllSavedSessionItems
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.UnselectSavedItem
import org.turter.patrocl.presentation.orders.edit.EditOrderViewModel
import org.turter.patrocl.presentation.orders.edit.VoidSelectorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOrderComponent(
    vm: EditOrderViewModel,
    state: EditOrderScreenState.Main
) {
    val newOrderItems = state.newOrderItems
    val orderInfo = state.orderInfo
    val selectedItem = state.getSelectedNewItem()
    val isSaving = state.isSaving
    val menuCurrentStatus = state.menuState.currentStatus
    val menuOpened = state.menuState.targetStatus is MenuStatus.Opened

    val focusManager = LocalFocusManager.current

    val itemListState = rememberLazyListState(
        initialFirstVisibleItemIndex = newOrderItems.size
    )

    var prevItemsSize by remember { mutableIntStateOf(state.newOrderItems.size) }

    LaunchedEffect(menuCurrentStatus) {
        if (menuCurrentStatus is MenuStatus.Opened) {
            itemListState.scrollToItem(itemListState.layoutInfo.totalItemsCount)
        }
    }

    LaunchedEffect(state.newOrderItems) {
        val currentSize = state.newOrderItems.size
        if (menuOpened && prevItemsSize < currentSize) {
            itemListState.animateScrollToItem(itemListState.layoutInfo.totalItemsCount)
        }
        prevItemsSize = currentSize
    }

    LaunchedEffect(menuOpened) {
        if (!menuOpened) {
            focusManager.clearFocus()
        }
    }

    val searchFieldInteractionSource = remember { MutableInteractionSource() }

    val searchIsPressed by searchFieldInteractionSource.collectIsPressedAsState()

    LaunchedEffect(searchIsPressed) {
        if (searchIsPressed && menuCurrentStatus is MenuStatus.Closed && !menuOpened) {
            vm.sendEvent(CommonOrderUiEvent.OpenMenu)
        }
    }

    val menuMaxHeight by animateFloatAsState(
        targetValue = if (menuOpened) 0.8f else 0f,
        animationSpec = tween(durationMillis = 500),
        finishedListener = {
            vm.sendEvent(
                CommonOrderUiEvent.SetMenuCurrentStatus(
                    if (it == 0f) MenuStatus.Closed
                    else MenuStatus.Opened
                )
            )
        }
    )

    val bottomSheetMenuOptions = listOf(
        MenuOption(
            label = "Свойства заказа",
            icon = Icons.Default.Info,
            onClick = { vm.sendEvent(CommonOrderUiEvent.OpenUpdateOrderInfo) }
        ),
        MenuOption(
            label = "Сохранить и продолжить",
            icon = Icons.Default.Create,
            onClick = { vm.sendEvent(EditOrderUiEvent.SaveOrderAndContinue) }
        ),
        MenuOption(
            label = "Сохранить и закрыть",
            icon = Icons.Default.Check,
            onClick = { vm.sendEvent(EditOrderUiEvent.SaveOrderAndFinish) }
        ),
        MenuOption(
            label = "Вернуться к заказам",
            icon = Icons.AutoMirrored.Default.ArrowBack,
            onClick = { vm.sendEvent(CommonOrderUiEvent.NavigateBack) }
        )
    )
    //Edit states

    val originOrder = state.order
    val sessions = originOrder.sessions
    val selected = state.selected

    val topAppBarState = when (selected) {
        is Selected.SavedItems -> EditOrderTopAppBarState.SavedItemsSelected(
            selectedItemsCount = selected.items.size,
            onClose = { vm.sendEvent(EditOrderUiEvent.UnselectAllSavedItems) },
            onDelete = { vm.sendEvent(EditOrderUiEvent.OpenVoidSelectorForSelectedItems) }
        )

        else -> EditOrderTopAppBarState.Default(
            orderName = originOrder.name,
            waiterName = originOrder.waiter.name,
            tableName = originOrder.table.name,
            onBack = { vm.sendEvent(CommonOrderUiEvent.NavigateBack) }
        )
    }

    Scaffold(
        topBar = { EditOrderTopAppBar(state = topAppBarState) },
        bottomBar = {
            OrderBottomActionBar(
                selectedState = state.selected,
                onOrderInfo = { vm.sendEvent(CommonOrderUiEvent.OpenUpdateOrderInfo) },
                onRestaurantMenu = { vm.sendEvent(CommonOrderUiEvent.SwitchMenu) },
                onSave = { vm.sendEvent(EditOrderUiEvent.SaveOrderAndContinue) },
                onFinish = { vm.sendEvent(EditOrderUiEvent.SaveOrderAndFinish) },
                onDefaultMenu = { vm.sendEvent(CommonOrderUiEvent.OpenBottomSheetMenu) },
                onUnselect = { vm.sendEvent(CommonOrderUiEvent.UnselectNewOrderItem) },
                onMoveUp = { vm.sendEvent(CommonOrderUiEvent.MoveSelectedItemUp) },
                onMoveDown = { vm.sendEvent(CommonOrderUiEvent.MoveSelectedItemDown) },
                onSelectedItemMenu = { vm.sendEvent(CommonOrderUiEvent.OpenBottomSheetMenu) }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                //items
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp, vertical = 4.dp),
                    state = itemListState
                ) {
                    savedItems(
                        sessions = sessions,
                        selectedState = selected,
                        voidSelectorOpen = { s, i ->
                            vm.sendEvent(OpenVoidSelectorForSessionItem(s, i))
                        },
                        openDishCategory = { vm.sendEvent(OpenDishCategory(it)) },
                        addSameAsNewItem = { vm.sendEvent(AddNewOrderItemByRkId(it)) },
                        selectItem = { s, i -> vm.sendEvent(SelectSavedItem(s, i)) },
                        unselectItem = { s, i -> vm.sendEvent(UnselectSavedItem(s, i)) },
                        selectAllSessionItems = { vm.sendEvent(SelectAllSavedSessionItems(it)) },
                        unselectAllSessionItems = { vm.sendEvent(UnselectAllSavedSessionItems(it)) }
                    )
                    newItems(
                        items = newOrderItems,
                        selectedItem = selectedItem,
                        commentInputOpen = { vm.sendEvent(OpenCommentInput(it)) },
                        removeItem = { vm.sendEvent(RemoveNewOrderItem(it)) },
                        incQnt = { vm.sendEvent(IncreaseNewOrderItemQuantity(it)) },
                        openQntInput = { vm.sendEvent(OpenQuantityInput(it)) },
                        openModSelector = { vm.sendEvent(OpenModifiersSelector(it)) },
                        openDishCategory = { vm.sendEvent(OpenDishCategory(it)) },
                        selectItem = { vm.sendEvent(SelectNewOrderItem(it)) },
                    )
                }
                //Menu
                RestaurantMenuSelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 70.dp)
                        .fillMaxHeight(menuMaxHeight),
                    categoriesMap = state.menu.categoryRkIdMap,
                    dishesMap = state.menu.dishRkIdMap,
                    stopListMap = state.menu.stopListDishRkIdMap,
                    rootCategoryRkId = state.menu.rootCategoryRkId,
                    searchFieldInteractionSource = searchFieldInteractionSource,
                    menuState = state.menuState,
                    setCurrentCategory = { vm.sendEvent(CommonOrderUiEvent.SetCurrentCategory(it)) },
                    switchMenuOpened = { vm.sendEvent(CommonOrderUiEvent.SwitchMenu) },
                    addNewItem = { vm.sendEvent(CommonOrderUiEvent.AddNewOrderItem(it)) }
                )
            }
        }

        if (state.customCommentInputState is CustomCommentInputState.Opened) {
            newOrderItems.find { state.customCommentInputState.uuid == it.uuid }?.let { item ->
                CreateCommentDialog(
                    modifiers = item.modifiers,
                    onConfirm = { vm.sendEvent(CommonOrderUiEvent.SaveComment(it)) },
                    onDismiss = { vm.sendEvent(CommonOrderUiEvent.CloseCommentInput) }
                )
            }
        }

        if (state.quantityInputState is QuantityInputState.Opened) {
            newOrderItems.find { state.quantityInputState.uuid == it.uuid }?.let { item ->
                RkQuantityInputDialog(
                    item = item,
                    onConfirm = { vm.sendEvent(CommonOrderUiEvent.SetQuantity(it)) },
                    onDismiss = { vm.sendEvent(CommonOrderUiEvent.CloseQuantityInput) }
                )
            }
        }

        state.interceptedAdding?.let { intercepted ->
            InterceptedAddingDialog(
                warningType = intercepted.warningType,
                onDismiss = { vm.sendEvent(CommonOrderUiEvent.RejectInterceptedAdding) },
                onConfirm = { vm.sendEvent(CommonOrderUiEvent.ConfirmInterceptedAdding) }
            )
        }

        when (val voidSelectorState = state.voidSelectorState) {
            is VoidSelectorState.ForSelectedItems -> {
                if (selected is Selected.SavedItems) RemoveSelectedOrderItemsDialog(
                    count = selected.items.size,
                    voids = state.menu.orderItemVoids,
                    onDismiss = { vm.sendEvent(EditOrderUiEvent.CloseVoidSelector) },
                    onConfirm = { vm.sendEvent(EditOrderUiEvent.RemoveAllSelectedSavedItems(it)) }
                )
            }

            is VoidSelectorState.ForSessionItem -> {
                val dish = voidSelectorState.item
                val dishInfo = state.menu.dishRkIdMap[dish.rkId]
                RemoveSessionOrderItemDialog(
                    itemName = dish.name,
                    initRkQnt = dish.rkQuantity,
                    maxQuantity = dish.rkQuantity,
                    qntDigits = dishInfo?.qntDecDigits ?: 1,
                    voids = state.menu.orderItemVoids,
                    onDismiss = { vm.sendEvent(EditOrderUiEvent.CloseVoidSelector) },
                    onConfirm = { rkQnt, voidRkId ->
                        vm.sendEvent(
                            EditOrderUiEvent.RemoveSessionItem(
                                voidSelectorState.session,
                                voidSelectorState.item,
                                rkQnt,
                                voidRkId
                            )
                        )
                    }
                )
            }

            else -> {}
        }
    }

    BottomSheetMenu(
        modifier = Modifier.fillMaxSize(),
        showBottomSheet = state.isBottomSheetOpened,
        onDismiss = { vm.sendEvent(CommonOrderUiEvent.HideBottomSheetMenu) },
        menuOptions = bottomSheetMenuOptions
    )

    FullscreenLoader(isShown = isSaving || state.isRemoving)
}

private fun Order.Dish.getKey(sessionUni: String) = "$sessionUni-$uni"