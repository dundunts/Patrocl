package org.turter.patrocl.data.dto.source.modifier.scheme

import kotlinx.serialization.Serializable
import org.turter.patrocl.data.dto.source.modifier.group.ModifierGroupInfoDto

@Serializable
data class ModifierSchemeInfoDto(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String,
    val autoOpen: Boolean,
    val details: List<Details>
) {
    @Serializable
    data class Details(
        val id: String,
        val rkId: String,
        val guid: String,
        val name: String,
        val modifiersGroupRkId: String,
        val defaultModifier: String,
        val upLimit: Int,
        val downLimit: Int,
        val freeCount: Boolean,
        val useUpLimit: Boolean,
        val useDownLimit: Boolean
    )
}
