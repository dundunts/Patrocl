package org.turter.patrocl.data.dto.source.modifier.scheme

import kotlinx.serialization.Serializable

@Serializable
data class CompanyModifiersSchemesData(
    val companyId: String,
    val schemesVersion: String,
    val schemesDetailsVersion: String,
    val modifiersSchemes: List<ModifierSchemeInfoDto>
)