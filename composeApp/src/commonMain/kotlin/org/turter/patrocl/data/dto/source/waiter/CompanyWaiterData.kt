package org.turter.patrocl.data.dto.source.waiter

import kotlinx.serialization.Serializable

@Serializable
data class CompanyWaiterData(
    val companyId: String,
    val version: String,
    val waiters: List<WaiterInfoDto>
)