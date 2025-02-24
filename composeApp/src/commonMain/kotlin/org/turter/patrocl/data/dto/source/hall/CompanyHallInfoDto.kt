package org.turter.patrocl.data.dto.source.hall

import kotlinx.serialization.Serializable
import org.turter.patrocl.domain.model.hall.HallType

@Serializable
data class CompanyHallInfoDto(
    val id: String,
    val rkId: String,
    val guid: String,
    val code: String,
    val name: String,
    val status: String,
    val mainParentIdent: String,
    val restaurant: String,
    val hallType: HallType,
    val tables: List<TableInfoDto>
)

