package org.turter.patrocl.data.dto.source.modifier.group

import kotlinx.serialization.Serializable

@Serializable
data class ModifierGroupInfoDto(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String,
    val commonModifier: Boolean,
    val childIds: List<String>,
    val modifierIds: List<String>
)
