package org.turter.patrocl.data.dto.source.hall

import kotlinx.serialization.Serializable
import org.turter.patrocl.domain.model.hall.HallInfo

@Serializable
data class CompanyHallsData(
    val companyId: String,
    val version: String,
    val defaultHallRkId: String,
    val halls: List<CompanyHallInfoDto>
)