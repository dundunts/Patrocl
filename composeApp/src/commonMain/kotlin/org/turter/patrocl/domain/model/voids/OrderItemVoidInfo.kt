package org.turter.patrocl.domain.model.voids

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemVoidInfo(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String
)