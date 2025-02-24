package org.turter.patrocl.domain.model.order

data class OrderItemForRemove(
    val dishId: String,
    val name: String,
    val code: String,
    val guid: String,
    val uni: String,
    val rkQuantity: Int,
    val voidId: String
) {
}