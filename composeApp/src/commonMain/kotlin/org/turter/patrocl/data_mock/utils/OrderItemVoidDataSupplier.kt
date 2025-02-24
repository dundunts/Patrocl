package org.turter.patrocl.data_mock.utils

import org.turter.patrocl.domain.model.voids.OrderItemVoidInfo

object OrderItemVoidDataSupplier {
    fun getOrderItemsVoids(): List<OrderItemVoidInfo> {
        return listOf(
            OrderItemVoidInfo(
                id = "1",
                rkId = "rk-1",
                guid = "guid-1",
                code = "code-1",
                name = "Со списанием",
                status = "ACTIVE",
                mainParentIdent = "mainParentIdent"
            ),
            OrderItemVoidInfo(
                id = "2",
                rkId = "rk-2",
                guid = "guid-2",
                code = "code-2",
                name = "Без списания",
                status = "ACTIVE",
                mainParentIdent = "mainParentIdent"
            ),
            OrderItemVoidInfo(
                id = "3",
                rkId = "rk-3",
                guid = "guid-3",
                code = "code-3",
                name = "Ошибка официанта",
                status = "ACTIVE",
                mainParentIdent = "mainParentIdent"
            ),
            OrderItemVoidInfo(
                id = "4",
                rkId = "rk-4",
                guid = "guid-4",
                code = "code-4",
                name = "Порча",
                status = "ACTIVE",
                mainParentIdent = "mainParentIdent"
            )
        )
    }
}