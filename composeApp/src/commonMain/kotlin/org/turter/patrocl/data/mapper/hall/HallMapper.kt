package org.turter.patrocl.data.mapper.hall

import io.realm.kotlin.ext.toRealmList
import org.turter.patrocl.data.dto.source.hall.CompanyHallInfoDto
import org.turter.patrocl.data.local.entity.hall.HallLocal
import org.turter.patrocl.domain.model.hall.HallInfo
import org.turter.patrocl.domain.model.hall.HallType

fun CompanyHallInfoDto.toHallInfo(): HallInfo = HallInfo(
    id = id,
    rkId = rkId,
    guid = guid,
    code = code,
    name = name,
    status = status,
    mainParentIdent = mainParentIdent,
    restaurant = restaurant,
    hallType = hallType,
    tables = tables.toTableInfoList()
)

fun CompanyHallInfoDto.toHallLocal(): HallLocal =
    HallLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.mainParentIdent = mainParentIdent
        target.restaurant = restaurant
        target.hallType = hallType.name
        target.tables = tables.map { it.toTableLocal() }.toRealmList()
        return@let target
    }

fun HallLocal.toHallInfo(): HallInfo = HallInfo(
    id = id,
    rkId = rkId,
    guid = guid,
    code = code,
    name = name,
    status = status,
    mainParentIdent = mainParentIdent,
    restaurant = restaurant,
    hallType = HallType.valueOf(hallType),
    tables = tables.toTableInfoListFromLocal()
)

fun List<CompanyHallInfoDto>.toHallInfoList(): List<HallInfo> = this.map { it.toHallInfo() }.toList()

fun List<CompanyHallInfoDto>.toHallLocalList(): List<HallLocal> =
    this.map { it.toHallLocal() }.toList()

fun List<HallLocal>.toHallInfoListFromLocal(): List<HallInfo> = this.map { it.toHallInfo() }.toList()