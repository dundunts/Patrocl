package org.turter.patrocl.presentation.orders.item.new.edit

import org.turter.patrocl.domain.model.menu.deprecated.DishDetailed
import org.turter.patrocl.domain.model.menu.deprecated.DishModifier
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.presentation.orders.common.interceptor.InterceptedAddingDish

data class EditNewOrderItemScreenState(
    val originalItem: NewOrderItem,
    val dish: DishDetailed,
    val quantity: Float,
    val modifiers: List<NewOrderItem.Modifier>,
    val targetModifier: DishModifier? = null,
    val targetModifierQuantity: Int = 1,
    val interceptedAdding: InterceptedAddingDish? = null
)