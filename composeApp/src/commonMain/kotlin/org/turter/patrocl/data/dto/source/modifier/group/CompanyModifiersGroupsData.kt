package org.turter.patrocl.data.dto.source.modifier.group

import kotlinx.serialization.Serializable

@Serializable
data class CompanyModifiersGroupsData(
    val companyId: String,
    val rootModifiersGroupId: String,
    val version: String,
    val modifiersGroups: List<ModifierGroupInfoDto>
)