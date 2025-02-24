package org.turter.patrocl.data.mapper.menu

import org.turter.patrocl.data.dto.source.modifier.list.CompanyStationModifierInfoDto
import org.turter.patrocl.data.local.entity.menu.ModifierLocal
import org.turter.patrocl.domain.model.menu.StationModifierInfo

fun CompanyStationModifierInfoDto.toModifierLocal(): ModifierLocal =
    ModifierLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.mainParentIdent = mainParentIdent
        target.maxOneDish = maxOneDish
        target.useLimitedQnt = useLimitedQnt
        target.price = price
        return@let target
    }

fun CompanyStationModifierInfoDto.toStationModifierInfo(): StationModifierInfo =
    StationModifierInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        maxOneDish = maxOneDish,
        useLimitedQnt = useLimitedQnt,
        price = price
    )

fun ModifierLocal.toStationModifierInfo(): StationModifierInfo =
    StationModifierInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        maxOneDish = maxOneDish,
        useLimitedQnt = useLimitedQnt,
        price = price
    )

fun List<CompanyStationModifierInfoDto>.toStationModifierInfoList(): List<StationModifierInfo> =
    this.map { it.toStationModifierInfo() }.toList()

fun List<CompanyStationModifierInfoDto>.toModifierLocalList(): List<ModifierLocal> =
    this.map { it.toModifierLocal() }.toList()

fun List<ModifierLocal>.toStationModifierInfoListFromLocal(): List<StationModifierInfo> =
    this.map { it.toStationModifierInfo() }.toList()