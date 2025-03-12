package org.turter.patrocl.presentation.orders.create.test

import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.order.NewOrderItem
import kotlin.random.Random
import kotlin.random.nextInt

//val initTestNewOrderItems = listOf(
//    NewOrderItem(
//        dishId = "dish-id-1",
//        dishName = "Цезарь с курицей",
//        rkQuantity = 1f,
//        modifiers = listOf(
//            NewOrderItem.Modifier.regular(
//                modifierId = "modifier-id-1",
//                name = "В ОДНУ ТАРЕЛКУ",
//                quantity = 1
//            ),
//            NewOrderItem.Modifier.regular(
//                modifierId = "modifier-id-2",
//                name = "ЗАМЕНА",
//                quantity = 2
//            ),
//            NewOrderItem.Modifier.comment(content = "Коммент 1"),
//            NewOrderItem.Modifier.comment(content = "Коммент 2")
//        )
//    ),
//    NewOrderItem(
//        dishId = "dish-id-2",
//        dishName = "Салат с куриной печенью",
//        rkQuantity = 2f,
//        modifiers = emptyList()
//    ),
//)

val testNewItems = createItems(3)

fun createItems(n: Int): List<NewOrderItem> {
    return buildList { for (i: Int in 0..n) {
        add(createItem(i))
    } }
}

fun createItem(i: Int): NewOrderItem = NewOrderItem(
    dishInfo = StationDishInfo(
        id = "rk-dish-id-$i",
        rkId = "rk-dish-id-$i",
        guid = "dish-guid-$i",
        code = "dish-code-$i",
        name = if (i % 4 == 0) "New item very-very long title, sure - yes-yes" else "New item $i",
        status = "ok",
        mainParentIdent = "category-id-$i",
        kurs = "",
        qntDecDigits = i % 4,
        modiScheme = "",
        comboScheme = "",
        categPath = "Пивная кружка ROOT/Салаты",
        price = 45000
    ),
    rkQuantity = Random(i).nextInt(500..15500),
    modifiers = if (i % 2 == 0) listOf(
        NewOrderItem.Modifier.regular(
            modifierId = "modifier-id-1",
            name = "В ОДНУ ТАРЕЛКУ",
            count = 1
        ),
        NewOrderItem.Modifier.regular(
            modifierId = "modifier-id-2",
            name = "ЗАМЕНА",
            count = 2
        ),
        NewOrderItem.Modifier.customComment(content = "Коммент 1"),
        NewOrderItem.Modifier.customComment(content = "Коммент 2")
    ) else if (i % 3 == 0) listOf(
        NewOrderItem.Modifier.regular(
            modifierId = "modifier-id-1",
            name = "В ОДНУ ТАРЕЛКУ",
            count = 1
        ),
        NewOrderItem.Modifier.regular(
            modifierId = "modifier-id-2",
            name = "ЗАМЕНА",
            count = 2
        )
    ) else listOf()
)