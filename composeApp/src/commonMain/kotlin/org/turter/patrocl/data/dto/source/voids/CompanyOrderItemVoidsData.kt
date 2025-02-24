package org.turter.patrocl.data.dto.source.voids

import kotlinx.serialization.Serializable

@Serializable
data class CompanyOrderItemVoidsData(
    val companyId: String,
    val version: String,
    val voids: List<CompanyOrderItemVoidInfoDto>
)