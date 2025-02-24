package org.turter.patrocl.data.dto.source.hall

import kotlinx.serialization.Serializable

@Serializable
data class TableInfoDto(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val hall: String,
    val tableGroup: String
)