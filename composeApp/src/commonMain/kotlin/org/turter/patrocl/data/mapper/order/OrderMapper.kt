package org.turter.patrocl.data.mapper.order

import org.turter.patrocl.data.dto.order.request.CreateOrderPayload
import org.turter.patrocl.data.dto.order.response.OrderDto
import org.turter.patrocl.data.dto.order.response.OrderPreviewDto
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.order.OrderPreview

fun OrderDto.toOrder(): Order = Order(
    guid = guid,
    name = name,
    table = table.toOrderTable(),
    waiter = waiter.toOrderWaiter(),
    creator = creator.toOrderWaiter(),
    openTime = openTime,
    sessions = sessions.map { it.toSession() }.toList(),
    rkSum = rkSum,
    rkUnpaidSum = rkUnpaidSum,
    rkDiscountSum = rkDiscountSum,
    paid = paid,
    finished = finished
)

fun OrderPreviewDto.toOrderPreview(): OrderPreview = OrderPreview(
    guid = guid,
    name = name,
    tableCode = tableCode,
    tableName = tableName,
    waiterCode = waiterCode,
    waiterName = waiterName,
    rkSum = rkSum,
    bill = bill,
    createTime = createTime,
    finishTime = finishTime
)

fun List<OrderPreviewDto>.toOrderList(): List<OrderPreview> = map(OrderPreviewDto::toOrderPreview)

fun toCreateOrderPayload(
    tableCode: String,
    waiterCode: String,
    orderItems: List<NewOrderItem>
): CreateOrderPayload {
    return CreateOrderPayload(
        tableCode = tableCode,
        waiterCode = waiterCode,
        dishList = orderItems.toOrderItemListPayload()
    )
}

fun OrderDto.Table.toOrderTable(): Order.Table = Order.Table(
    rkId = id,
    code = code,
    name = name,
    guid = guid
)

fun OrderDto.Waiter.toOrderWaiter(): Order.Waiter = Order.Waiter(
    rkId = id,
    code = code,
    name = name,
    guid = guid
)