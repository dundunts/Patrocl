package org.turter.patrocl.data.dto.source.category

import kotlinx.serialization.Serializable

@Serializable
data class CompanyCategoriesData(
    val companyId: String,
    val rootCategoryRkId: String,
    val version: String,
    val categories: List<CategoryInfoDto>
)

