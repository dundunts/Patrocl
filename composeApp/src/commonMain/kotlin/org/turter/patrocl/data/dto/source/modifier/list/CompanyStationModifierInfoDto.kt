package org.turter.patrocl.data.dto.source.modifier.list

import kotlinx.serialization.Serializable

@Serializable
data class CompanyStationModifierInfoDto(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String,
    val maxOneDish: Int,
    val useLimitedQnt: Boolean,
    val inputName: Boolean,
    val price: Int
)