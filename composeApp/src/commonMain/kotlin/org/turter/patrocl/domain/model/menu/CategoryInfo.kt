package org.turter.patrocl.domain.model.menu

data class CategoryInfo(
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
