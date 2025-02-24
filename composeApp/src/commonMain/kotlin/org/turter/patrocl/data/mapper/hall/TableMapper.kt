package org.turter.patrocl.data.mapper.hall

import org.turter.patrocl.data.dto.source.hall.TableInfoDto
import org.turter.patrocl.data.local.entity.hall.TableLocal
import org.turter.patrocl.domain.model.hall.TableInfo
import org.turter.patrocl.domain.model.order.Order

fun TableInfoDto.toTableInfo(): TableInfo = TableInfo(
    id = id,
    rkId = rkId,
    guid = guid,
    code = code,
    name = name,
    status = status,
    hall = hall,
    tableGroup = tableGroup
)

fun TableInfoDto.toTableLocal(): TableLocal =
    TableLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.hall = hall
        target.tableGroup = tableGroup
        return@let target
    }

fun TableLocal.toTableInfo() = TableInfo(
    id = id,
    rkId = rkId,
    guid = guid,
    code = code,
    name = name,
    status = status,
    hall = hall,
    tableGroup = tableGroup
)

fun List<TableInfoDto>.toTableInfoList(): List<TableInfo> = this.map { it.toTableInfo() }.toList()

fun List<TableInfoDto>.toTableLocalList(): List<TableLocal> = this.map { it.toTableLocal() }.toList()

fun List<TableLocal>.toTableInfoListFromLocal(): List<TableInfo> = this.map { it.toTableInfo() }.toList()

fun TableInfo.toOrderTable(): Order.Table = Order.Table(
    id = rkId,
    code = code,
    name = name,
    guid = guid
)