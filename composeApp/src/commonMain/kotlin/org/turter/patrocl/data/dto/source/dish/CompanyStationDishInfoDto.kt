package org.turter.patrocl.data.dto.source.dish

import kotlinx.serialization.Serializable

@Serializable
data class CompanyStationDishInfoDto(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String,
    val kurs: String,
    val qntDecDigits: Int,
    val modiScheme: String,
    val comboScheme: String,
    val categPath: String,
    val price: Int
)