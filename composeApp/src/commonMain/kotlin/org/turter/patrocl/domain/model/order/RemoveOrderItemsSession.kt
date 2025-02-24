package org.turter.patrocl.domain.model.order

data class RemoveOrderItemsSession(
    val uni: String,
    val lineGuid: String,
    val sessionId: String,
    val itemsForRemove: List<OrderItemForRemove>
)
