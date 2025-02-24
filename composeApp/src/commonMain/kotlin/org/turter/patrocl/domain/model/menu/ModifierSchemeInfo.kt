package org.turter.patrocl.domain.model.menu

data class ModifierSchemeInfo(
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
    data class Details(
        val id: String,
        val rkId: String,
        val guid: String,
        val code: String,
        val name: String,
        val status: String,
        val mainParentIdent: String,
        val modifiersGroupRkId: String,
        val defaultModifier: String,
        val upLimit: Int,
        val downLimit: Int,
        val freeCount: Boolean
    )
}
