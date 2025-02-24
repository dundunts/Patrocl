package org.turter.patrocl.domain.model.hall

data class HallInfo(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String,
    val restaurant: String,
    val hallType: HallType,
    val tables: List<TableInfo>
)