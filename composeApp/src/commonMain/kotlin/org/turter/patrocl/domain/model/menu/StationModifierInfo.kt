package org.turter.patrocl.domain.model.menu

data class StationModifierInfo(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String,
    val maxOneDish: Int,
    val useLimitedQnt: Boolean,
    val price: Int
)