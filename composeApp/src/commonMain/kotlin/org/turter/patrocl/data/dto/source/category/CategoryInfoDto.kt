package org.turter.patrocl.data.dto.source.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryInfoDto(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String,
    val childIds: List<String>,
    val dishIds: List<String>
)