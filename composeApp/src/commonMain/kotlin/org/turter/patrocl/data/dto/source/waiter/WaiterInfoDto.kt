package org.turter.patrocl.data.dto.source.waiter

import kotlinx.serialization.Serializable

@Serializable
data class WaiterInfoDto(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String
)