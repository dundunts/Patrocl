package org.turter.patrocl.data.dto.order.request

import kotlinx.serialization.Serializable

sealed interface OrderItemPayload {
    val dishId: String
    val rkQuantity: Int

    @Serializable
    data class Add(
        override val dishId: String,
        override val rkQuantity: Int,
        val modifiers: List<ModifierPayload>
    ) : OrderItemPayload

    @Serializable
    data class Remove(
        override val dishId: String,
        val dishCode: String,
        val dishUni: String,
        val voidId: String,
        override val rkQuantity: Int
    ) : OrderItemPayload
}
