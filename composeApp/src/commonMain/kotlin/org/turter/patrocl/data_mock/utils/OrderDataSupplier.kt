package org.turter.patrocl.data_mock.utils

import kotlinx.datetime.LocalDateTime
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.order.OrderPreview
import org.turter.patrocl.utils.now

object OrderDataSupplier {

    fun getOrder(): Order {
        val waiter = Order.Waiter(id = "waiter-id-1", code = "99", name = "Бобби", guid = "guid-1")
        return Order(
            guid = "order-guid-1",
            name = "33.1",
            rkSum = 123450,
            table = Order.Table(id = "table-id-1", name = "33", code = "table-code-1", guid = "guid-1"),
            waiter = waiter,
            openTime = LocalDateTime.now(),
            rkUnpaidSum = 123450,
            rkDiscountSum = 123450,
            paid = false,
            finished = false,
            creator = waiter,
            sessions = listOf(
                Order.Session(
                    uni = "session-uni-1",
                    lineGuid = "session-lineGuid-1",
                    sessionId = "session-sessionId-1",
                    isDraft = false,
                    remindTime = LocalDateTime.now(),
                    startService = LocalDateTime.now(),
                    printed = true,
                    cookMins = 0,
                    creator = waiter,
                    dishes = listOf(
                        Order.Dish(
                            id = "dish-id-1",
                            name = "Цезарь с курицей",
                            rkQuantity = 1000,
                            code = "dish-code-1",
                            guid = "guid-1",
                            rkPrice = 22000,
                            rkAmount = 22000,
                            rkPriceListAmount = 22000,
                            uni = "dish-uni-1-1",
                            modifiers = listOf()
                        ),
                        Order.Dish(
                            id = "dish-id-3",
                            name = "Буритто",
                            rkQuantity = 2000,
                            code = "dish-code-2",
                            guid = "guid-2",
                            rkPrice = 44000,
                            rkAmount = 88000,
                            rkPriceListAmount = 88000,
                            uni = "dish-uni-1-2",
                            modifiers = listOf(
                                Order.Dish.Modifier(
                                    id = "modifier-id-1",
                                    name = "В ОДНУ ТАРЕЛКУ",
                                    code = "code-1",
                                    guid = "guid-1",
                                    rkAmount = 0,
                                    count = 1
                                )
                            )
                        )
                    )
                ),
                Order.Session(
                    uni = "session-uni-2",
                    lineGuid = "session-lineGuid-2",
                    sessionId = "session-sessionId-2",
                    isDraft = false,
                    remindTime = LocalDateTime.now(),
                    startService = LocalDateTime.now(),
                    printed = true,
                    cookMins = 0,
                    creator = Order.Waiter(id = "waiter-id-2", code = "133", name = "Билли", guid = "guid-1"),
                    dishes = listOf(
                        Order.Dish(
                            id = "dish-id-3",
                            name = "Буритто",
                            code = "dish-code-3",
                            rkQuantity = 2000,
                            guid = "guid-2",
                            rkPrice = 44000,
                            rkAmount = 88000,
                            rkPriceListAmount = 88000,
                            uni = "dish-uni-2-1",
                            modifiers = listOf()
                        ),
                        Order.Dish(
                            id = "dish-id-4",
                            name = "Куриная отбивная",
                            code = "dish-code-4",
                            rkQuantity = 1000,
                            guid = "guid-2",
                            rkPrice = 442000,
                            rkAmount = 442000,
                            rkPriceListAmount = 442000,
                            uni = "dish-uni-2-2",
                            modifiers = listOf(
                                Order.Dish.Modifier(
                                    id = "modifier-id-2",
                                    name = "ЗАМЕНА",
                                    code = "code-2",
                                    guid = "guid-2",
                                    rkAmount = 0,
                                    count = 3
                                ),
                            )
                        ),
                        Order.Dish(
                            id = "dish-id-5",
                            name = "Греча",
                            code = "dish-code-5",
                            rkQuantity = 1000,
                            guid = "guid-2",
                            rkPrice = 42000,
                            rkAmount = 42000,
                            rkPriceListAmount = 42000,
                            uni = "dish-uni-2-3",
                            modifiers = listOf(
                                Order.Dish.Modifier(
                                    id = "modifier-id-1",
                                    name = "В ОДНУ ТАРЕЛКУ",
                                    code = "code-3",
                                    guid = "guid-3",
                                    rkAmount = 0,
                                    count = 1
                                ),
                                Order.Dish.Modifier(
                                    id = "modifier-id-5",
                                    name = "Не зажаривать",
                                    code = "code-4",
                                    guid = "guid-4",
                                    rkAmount = 0,
                                    count = 1
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    fun getOrderPreviews() = listOf(
        OrderPreview(
            guid = "order-guid-1",
            name = "33.1",
            tableCode = "table-code-1",
            tableName = "33",
            waiterCode = "99",
            waiterName = "Бобби",
            rkSum = 123450,
            bill = false,
            openTime = LocalDateTime.now()
        ),
        OrderPreview(
            guid = "order-guid-2",
            name = "34",
            tableCode = "table-code-2",
            tableName = "34",
            waiterCode = "133",
            waiterName = "Билли",
            rkSum = 133650,
            bill = false,
            openTime = LocalDateTime.now()
        ),
        OrderPreview(
            guid = "order-guid-3",
            name = "34.1",
            tableCode = "table-code-2",
            tableName = "34",
            waiterCode = "133",
            waiterName = "Билли",
            rkSum = 2589950,
            bill = true,
            openTime = LocalDateTime.now()
        ),
        OrderPreview(
            guid = "order-guid-4",
            name = "54",
            tableCode = "table-code-3",
            tableName = "54",
            waiterCode = "99",
            waiterName = "Бобби",
            rkSum = 456200,
            bill = false,
            openTime = LocalDateTime.now()
        ),
        OrderPreview(
            guid = "order-guid-5",
            name = "21",
            tableCode = "table-code-4",
            tableName = "21",
            waiterCode = "99",
            waiterName = "Бобби",
            rkSum = 12678250,
            bill = true,
            openTime = LocalDateTime.now()
        )
    )

}