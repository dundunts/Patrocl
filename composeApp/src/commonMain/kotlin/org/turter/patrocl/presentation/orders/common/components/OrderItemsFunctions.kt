package org.turter.patrocl.presentation.orders.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.presentation.orders.common.Selected

fun LazyListScope.newItems(
    items: List<NewOrderItem>,
    selectedItem: NewOrderItem?,
    commentInputOpen: (uuid: Uuid) -> Unit,
    removeItem: (uuid: Uuid) -> Unit,
    incQnt: (item: NewOrderItem) -> Unit,
    openQntInput: (uuid: Uuid) -> Unit,
    openModSelector: (item: NewOrderItem) -> Unit,
    openDishCategory: (dishRkId: String) -> Unit,
    selectItem: (uuid: Uuid) -> Unit
) {
    item(key = "newItemsTitle") {
        OrderItemsGroupsDivider(
            modifier = Modifier.animateItem(),
            title = "Новая сессия"
        )
    }
    items(items = items, key = { it.uuid }) { orderItem ->
        SwipeToDismissWrapper(
            modifier = Modifier.animateItem(),
            onStartToEnd = {
                commentInputOpen(orderItem.uuid)
                false
            },
            onEndToStart = {
                removeItem(orderItem.uuid)
                false
            }
        ) {
            NewOrderItemCard(
                item = orderItem,
                enabled = true,
                select = orderItem.uuid == selectedItem?.uuid,
                onCountClick = { incQnt(orderItem) },
                onCountLongClick = { openQntInput(orderItem.uuid) },
                onTitleClick = { openModSelector(orderItem) },
                onTitleDoubleClick = { openDishCategory(orderItem.dishInfo.rkId) },
                onTitleLongClick = { selectItem(orderItem.uuid) }
            )
        }
    }
}

fun LazyListScope.savedItems(
    sessions: List<Order.Session>,
    selectedState: Selected,
    voidSelectorOpen: (session: Order.Session, item: Order.Dish) -> Unit,
    openDishCategory: (dishRkId: String) -> Unit,
    addSameAsNewItem: (dishRkId: String) -> Unit,
    selectItem: (session: Order.Session, item: Order.Dish) -> Unit,
    unselectItem: (session: Order.Session, item: Order.Dish) -> Unit,
    selectAllSessionItems: (session: Order.Session) -> Unit,
    unselectAllSessionItems: (session: Order.Session) -> Unit
) {
    sessions.forEach { session ->
        val sessionIsSelected = sessionItemsSelected(selectedState, session)
        val sessionDishes = session.dishes.filter { it.rkQuantity > 0 }
        item(key = session.uni) {
            SessionCard(
                modifier = Modifier.animateItem(),
                session = session,
                select = sessionIsSelected,
                onLongClick = {
                    if (sessionIsSelected) unselectAllSessionItems(session)
                    else selectAllSessionItems(session)
                }
            )
        }
        if (sessionDishes.isNotEmpty()) {
            items(items = session.dishes, key = { session.uni + it.uni }) { dish ->
                val itemIsSelected =
                    sessionIsSelected || itemIsSelected(selectedState, session, dish)
                SavedOrderItemCard(
                    modifier = Modifier.animateItem(),
                    item = dish,
                    enabled = true,
                    select = itemIsSelected,
                    onCountLongClick = {
                        voidSelectorOpen(session, dish)
                    },
                    onTitleClick = {
                        addSameAsNewItem(dish.rkId)
                    },
                    onTitleDoubleClick = {
                        openDishCategory(dish.rkId)
                    },
                    onTitleLongClick = {
                        if (itemIsSelected) unselectItem(session, dish)
                        else selectItem(session, dish)
                    }
                )
            }
        }
    }
}

private fun itemIsSelected(selected: Selected, session: Order.Session, item: Order.Dish): Boolean {
    return if (selected is Selected.SavedItems) selected.contains(session, item) else false
}

private fun sessionItemsSelected(selected: Selected, session: Order.Session): Boolean {
    return if (selected is Selected.SavedItems) {
        session.dishes.filter { it.rkQuantity > 0 }.takeIf { it.isNotEmpty() }?.all { dish ->
            selected.contains(session, dish)
        } ?: false
    } else false
}

private fun Selected.SavedItems.contains(session: Order.Session, item: Order.Dish) =
    this.items.any { it.sessionUni == session.uni && it.dishUni == item.uni }