package org.turter.patrocl.data.mapper.order

import org.turter.patrocl.data.dto.order.request.OrderSessionPayload
import org.turter.patrocl.data.dto.order.request.RemoveItemsFromOrderPayload
import org.turter.patrocl.data.dto.order.response.OrderDto
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.order.RemoveOrderItemsSession

fun OrderDto.Session.toSession() =
    Order.Session(
        uni = uni,
        lineGuid = lineGuid,
        sessionId = sessionId,
        isDraft = isDraft,
        remindTime = remindTime,
        startService = startService,
        printed = printed,
        cookMins = cookMins,
        creator = creator.toOrderWaiter(),
        dishes = dishes.map { it.toDish() }.toList()
    )

fun OrderDto.Dish.toDish() = Order.Dish(
    rkId = id,
    name = name,
    guid = guid,
    rkQuantity = rkQuantity,
    rkPrice = rkPrice,
    rkAmount = rkAmount,
    rkPriceListAmount = rkPriceListAmount,
    code = code,
    uni = uni,
    modifiers = modifiers.map { it.toModifier() }.toList()
)

fun OrderDto.Dish.Modifier.toModifier() = Order.Dish.Modifier(
    rkId = id,
    name = name,
    guid = guid,
    code = code,
    count = count,
    rkAmount = rkAmount
)

fun List<NewOrderItem>.toAddItemsPayload(orderGuid: String): OrderSessionPayload.AddDishes =
    OrderSessionPayload.AddDishes(
        orderGuid = orderGuid,
        items = this.toOrderItemListPayload()
    )

fun RemoveOrderItemsSession.toOrderSessionPayloadRemoveDishes(): OrderSessionPayload.RemoveDishes =
    OrderSessionPayload.RemoveDishes(
        sessionUni = uni,
        items = itemsForRemove.map { it.toRemoveItemPayload() }
    )

fun List<RemoveOrderItemsSession>.toOrderSessionPayloadRemoveDishesList()
        : List<OrderSessionPayload.RemoveDishes> =
    this.map { it.toOrderSessionPayloadRemoveDishes() }.toList()

fun RemoveOrderItemsSession.toRemoveItemsFromOrderPayload(orderGuid: String)
        : RemoveItemsFromOrderPayload =
    RemoveItemsFromOrderPayload(
        orderGuid = orderGuid,
        sessions = listOf(this.toOrderSessionPayloadRemoveDishes())
    )

fun List<RemoveOrderItemsSession>.toRemoveItemsFromOrderPayload(orderGuid: String)
        : RemoveItemsFromOrderPayload =
    RemoveItemsFromOrderPayload(
        orderGuid = orderGuid,
        sessions = this.toOrderSessionPayloadRemoveDishesList()
    )