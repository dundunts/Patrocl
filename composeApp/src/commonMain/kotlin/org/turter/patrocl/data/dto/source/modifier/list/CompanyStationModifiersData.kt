package org.turter.patrocl.data.dto.source.modifier.list

import kotlinx.serialization.Serializable

@Serializable
data class CompanyStationModifiersData(
    val companyId: String,
    val version: String,
    val modifiers: List<CompanyStationModifierInfoDto>
)