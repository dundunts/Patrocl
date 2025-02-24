package org.turter.patrocl.data.mapper.voids

import org.turter.patrocl.data.dto.source.voids.CompanyOrderItemVoidInfoDto
import org.turter.patrocl.data.local.entity.voids.OrderItemVoidLocal
import org.turter.patrocl.domain.model.voids.OrderItemVoidInfo

fun CompanyOrderItemVoidInfoDto.toOrderItemVoidInfo(): OrderItemVoidInfo =
    OrderItemVoidInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent
    )

fun CompanyOrderItemVoidInfoDto.toOrderItemVoidLocal(): OrderItemVoidLocal =
    OrderItemVoidLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.mainParentIdent = mainParentIdent
        return@let target
    }

fun OrderItemVoidLocal.toOrderItemVoidInfo(): OrderItemVoidInfo =
    OrderItemVoidInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent
    )

fun List<CompanyOrderItemVoidInfoDto>.toOrderItemVoidInfoList(): List<OrderItemVoidInfo> =
    this.map { it.toOrderItemVoidInfo() }.toList()

fun List<CompanyOrderItemVoidInfoDto>.toOrderItemVoidLocalList(): List<OrderItemVoidLocal> =
    this.map { it.toOrderItemVoidLocal() }.toList()

fun List<OrderItemVoidLocal>.toOrderItemVoidInfoListFromLocal(): List<OrderItemVoidInfo> =
    this.map { it.toOrderItemVoidInfo() }.toList()