package org.turter.patrocl.data.dto.source.dish

import kotlinx.serialization.Serializable

@Serializable
data class CompanyStationDishesData(
    val companyId: String,
    val version: String,
    val dishes: List<CompanyStationDishInfoDto>
)