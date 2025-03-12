package org.turter.patrocl.data.mapper.order

import org.turter.patrocl.data.dto.order.request.ModifierPayload
import org.turter.patrocl.data.dto.order.request.OrderItemPayload
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.order.OrderItemForRemove

fun OrderItemForRemove.toRemoveItemPayload(): OrderItemPayload.Remove =
    OrderItemPayload.Remove(
        dishId = dishId,
        dishCode = code,
        dishUni = uni,
        voidId = voidId,
        rkQuantity = rkQuantity
    )

fun List<OrderItemForRemove>.toRemoveItemPayloadList(): List<OrderItemPayload.Remove> =
    this.map { it.toRemoveItemPayload() }.toList()

fun List<NewOrderItem>.toOrderItemListPayload(): List<OrderItemPayload.Add> =
    map { item ->
        OrderItemPayload.Add(
            dishId = item.dishInfo.rkId,
            rkQuantity = item.rkQuantity,
            modifiers = item.modifiers.toModifierListPayload()
        )
    }

fun List<NewOrderItem.Modifier>.toModifierListPayload(): List<ModifierPayload> =
    map { modifier ->
        ModifierPayload(
            type = modifier.type,
            modifierId = modifier.modifierId,
            count = modifier.count,
            content = modifier.content
        )
    }

fun List<Order.Dish>.toRemoveItemsPayload(voidId: String): List<OrderItemPayload.Remove> =
    this.map { dish ->
        OrderItemPayload.Remove(
            dishId = dish.rkId,
            dishCode = dish.code,
            dishUni = dish.uni,
            rkQuantity = dish.rkQuantity,
            voidId = voidId
        )
    }