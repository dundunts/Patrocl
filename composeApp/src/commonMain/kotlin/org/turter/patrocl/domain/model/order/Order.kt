package org.turter.patrocl.domain.model.order

import kotlinx.datetime.LocalDateTime

data class Order(
    val guid: String,
    val name: String,
    val rkSum: Int,
    val rkUnpaidSum: Int,
    val rkDiscountSum: Int,
    val paid: Boolean,
    val finished: Boolean,
    val table: Table,
    val creator: Waiter,
    val waiter: Waiter,
    val openTime: LocalDateTime,
    val sessions: List<Session>
) {
    data class Waiter(
        val id: String,
        val code: String,
        val name: String,
        val guid: String
    )

    data class Table(
        val id: String,
        val code: String,
        val name: String,
        val guid: String
    )

    data class Session(
        val uni: String,
        val lineGuid: String,
        val sessionId: String,
        val isDraft: Boolean,
        val remindTime: LocalDateTime,
        val startService: LocalDateTime,
        val printed: Boolean,
        val cookMins: Int,
        val creator: Waiter,
        val dishes: List<Dish>
    )

    data class Dish(
        val id: String,
        val name: String,
        val code: String,
        val guid: String,
        val uni: String,
        val rkQuantity: Int,
        val rkPrice: Int,
        val rkAmount: Int,
        val rkPriceListAmount: Int,
        val modifiers: List<Modifier>
    ) {
        data class Modifier(
            val id: String,
            val name: String,
            val code: String,
            val guid: String,
            val count: Int,
            val rkAmount: Int
        )
    }
}