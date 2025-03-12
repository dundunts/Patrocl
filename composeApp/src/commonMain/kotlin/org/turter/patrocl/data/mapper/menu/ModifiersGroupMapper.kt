package org.turter.patrocl.data.mapper.menu

import io.realm.kotlin.ext.toRealmList
import org.turter.patrocl.data.dto.source.modifier.group.ModifierGroupInfoDto
import org.turter.patrocl.data.local.entity.menu.ModifiersGroupLocal
import org.turter.patrocl.domain.model.menu.ModifierGroupInfo
import org.turter.patrocl.domain.model.menu.deprecated.DishModifier
import org.turter.patrocl.domain.model.menu.deprecated.ModifiersGroup
import org.turter.patrocl.domain.model.menu.deprecated.ModifiersGroupDetailed

fun ModifierGroupInfoDto.toModifiersGroupInfo(): ModifierGroupInfo =
    ModifierGroupInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        commonModifier = commonModifier,
        childIds = childIds.toList(),
        modifierIds = modifierIds.toList()
    )

fun ModifierGroupInfoDto.toModifiersGroupLocal(): ModifiersGroupLocal =
    ModifiersGroupLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.mainParentIdent = mainParentIdent
        target.commonModifier = commonModifier
        target.childIds = childIds.toRealmList()
        target.modifierIds = modifierIds.toRealmList()
        return@let target
    }

fun ModifiersGroupLocal.toModifiersGroupInfo(): ModifierGroupInfo =
    ModifierGroupInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        commonModifier = commonModifier,
        childIds = childIds.toList(),
        modifierIds = modifierIds.toList()
    )

fun List<ModifierGroupInfoDto>.toModifiersGroupInfoList(): List<ModifierGroupInfo> =
    this.map { it.toModifiersGroupInfo() }.toList()

fun List<ModifierGroupInfoDto>.toModifiersGroupLocalList(): List<ModifiersGroupLocal> =
    this.map { it.toModifiersGroupLocal() }.toList()

fun List<ModifiersGroupLocal>.toModifiersGroupInfoListFromLocal(): List<ModifierGroupInfo> =
    this.map { it.toModifiersGroupInfo() }.toList()

fun ModifiersGroup.toDetailed(
    parent: ModifiersGroupDetailed?,
    allModifiers: List<DishModifier>
): ModifiersGroupDetailed {
    val result = ModifiersGroupDetailed(
        id = this.id,
        guid = this.guid,
        code = this.code,
        name = this.name,
        status = this.status,
        parent = parent,
        childList = emptyList(),
        modifiers = allModifiers
            .filter { modifier -> this.modifierIdList.contains(modifier.id) }
            .toList()
    )
    result.childList = this.childList
        .map { group -> group.toDetailed(result, allModifiers) }
        .toList()
    return result
}