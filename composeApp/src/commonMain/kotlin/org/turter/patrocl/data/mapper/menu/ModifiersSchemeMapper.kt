package org.turter.patrocl.data.mapper.menu

import io.realm.kotlin.ext.toRealmList
import org.turter.patrocl.data.dto.source.modifier.scheme.ModifierSchemeInfoDto
import org.turter.patrocl.data.local.entity.menu.ModifiersSchemeDetailsLocal
import org.turter.patrocl.data.local.entity.menu.ModifiersSchemeLocal
import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo

fun ModifierSchemeInfoDto.toModifiersSchemeInfo(): ModifierSchemeInfo =
    ModifierSchemeInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        autoOpen = autoOpen,
        details = details.toModifiersSchemeInfoDetailsList()
    )

fun ModifierSchemeInfoDto.toModifiersSchemeLocal(): ModifiersSchemeLocal =
    ModifiersSchemeLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.mainParentIdent = mainParentIdent
        target.autoOpen = autoOpen
        target.details = details.toModifiersSchemeDetailsLocalList().toRealmList()
        return@let target
    }

fun ModifiersSchemeLocal.toModifiersSchemeInfo(): ModifierSchemeInfo =
    ModifierSchemeInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        autoOpen = autoOpen,
        details = details.toModifiersSchemeInfoDetailsListFromLocal()
    )

fun List<ModifierSchemeInfoDto>.toModifiersSchemeInfoList(): List<ModifierSchemeInfo> =
    this.map { it.toModifiersSchemeInfo() }.toList()

fun List<ModifierSchemeInfoDto>.toModifiersSchemeLocalList(): List<ModifiersSchemeLocal> =
    this.map { it.toModifiersSchemeLocal() }.toList()

fun List<ModifiersSchemeLocal>.toModifiersSchemeInfoListFromLocal(): List<ModifierSchemeInfo> =
    this.map { it.toModifiersSchemeInfo() }.toList()

fun ModifierSchemeInfoDto.Details.toModifiersSchemeInfoDetails(): ModifierSchemeInfo.Details =
    ModifierSchemeInfo.Details(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        modifiersGroupRkId = modifiersGroup.rkId,
        defaultModifier = defaultModifier,
        upLimit = upLimit,
        downLimit = downLimit,
        freeCount = freeCount
    )

fun ModifierSchemeInfoDto.Details.toModifiersSchemeDetailsLocal(): ModifiersSchemeDetailsLocal =
    ModifiersSchemeDetailsLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.mainParentIdent = mainParentIdent
        target.modifiersGroupRkId = modifiersGroup.rkId
        target.defaultModifier = defaultModifier
        target.upLimit = upLimit
        target.downLimit = downLimit
        target.freeCount = freeCount
        return@let target
    }

fun ModifiersSchemeDetailsLocal.toModifiersSchemeInfoDetails(): ModifierSchemeInfo.Details =
    ModifierSchemeInfo.Details(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        modifiersGroupRkId = modifiersGroupRkId,
        defaultModifier = defaultModifier,
        upLimit = upLimit,
        downLimit = downLimit,
        freeCount = freeCount
    )

fun List<ModifierSchemeInfoDto.Details>.toModifiersSchemeInfoDetailsList()
        : List<ModifierSchemeInfo.Details> =
    this.map { it.toModifiersSchemeInfoDetails() }.toList()

fun List<ModifierSchemeInfoDto.Details>.toModifiersSchemeDetailsLocalList()
        : List<ModifiersSchemeDetailsLocal> =
    this.map { it.toModifiersSchemeDetailsLocal() }.toList()

fun List<ModifiersSchemeDetailsLocal>.toModifiersSchemeInfoDetailsListFromLocal()
        : List<ModifierSchemeInfo.Details> =
    this.map { it.toModifiersSchemeInfoDetails() }.toList()