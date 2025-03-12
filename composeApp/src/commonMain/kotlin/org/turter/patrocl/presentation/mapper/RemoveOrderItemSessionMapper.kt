package org.turter.patrocl.presentation.mapper

import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.order.OrderItemForRemove
import org.turter.patrocl.domain.model.order.RemoveOrderItemsSession

fun buildRemoveSession(session: Order.Session, item: Order.Dish, rqQnt: Int, voidId: String) =
    RemoveOrderItemsSession(
        uni = session.uni,
        lineGuid = session.lineGuid,
        sessionId = session.sessionId,
        itemsForRemove = listOf(item.toOrderItemForRemove(voidId, rqQnt))
    )

fun buildRemoveSession(session: Order.Session, voidId: String) =
    RemoveOrderItemsSession(
        uni = session.uni,
        lineGuid = session.lineGuid,
        sessionId = session.sessionId,
        itemsForRemove = session.dishes.toOrderItemForRemoveList(voidId)
    )

fun buildRemoveSession(session: Order.Session, items: List<Order.Dish>, voidId: String) =
    RemoveOrderItemsSession(
        uni = session.uni,
        lineGuid = session.lineGuid,
        sessionId = session.sessionId,
        itemsForRemove = items.toOrderItemForRemoveList(voidId)
    )

fun List<Order.Dish>.toOrderItemForRemoveList(voidId: String): List<OrderItemForRemove> =
    this.map { it.toOrderItemForRemove(voidId) }.toList()

fun Order.Dish.toOrderItemForRemove(voidId: String, qnt: Int = rkQuantity): OrderItemForRemove =
    OrderItemForRemove(
        dishId = rkId,
        name = name,
        code = code,
        guid = guid,
        uni = uni,
        rkQuantity = qnt,
        voidId = voidId
    )