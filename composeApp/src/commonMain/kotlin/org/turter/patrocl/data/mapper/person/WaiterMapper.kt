package org.turter.patrocl.data.mapper.person

import org.turter.patrocl.data.dto.source.waiter.WaiterInfoDto
import org.turter.patrocl.data.local.entity.person.WaiterLocal
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.person.Waiter

fun WaiterInfoDto.toWaiter(): Waiter = Waiter(
    id = id,
    rkId = rkId,
    guid = guid,
    code = code,
    name = name
)

fun WaiterInfoDto.toWaiterLocal() : WaiterLocal =
    WaiterLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.mainParentIdent = mainParentIdent
        return@let target
    }

fun WaiterLocal.toWaiter() : Waiter = Waiter(
    id = id,
    rkId = rkId,
    guid = guid,
    code = code,
    name = name
)

fun List<WaiterInfoDto>.toWaiterList(): List<Waiter> = this.map { it.toWaiter() }.toList()

fun List<WaiterInfoDto>.toWaiterLocalList(): List<WaiterLocal> =
    this.map { it.toWaiterLocal() }.toList()

fun List<WaiterLocal>.toWaiterListFromLocal(): List<Waiter> = this.map { it.toWaiter() }.toList()

fun Waiter.toOrderWaiter(): Order.Waiter = Order.Waiter(
    rkId = rkId,
    code = code,
    name = name,
    guid = guid
)