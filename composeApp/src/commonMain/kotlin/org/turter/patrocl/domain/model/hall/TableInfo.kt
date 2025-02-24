package org.turter.patrocl.domain.model.hall

data class TableInfo(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val hall: String,
    val tableGroup: String
)