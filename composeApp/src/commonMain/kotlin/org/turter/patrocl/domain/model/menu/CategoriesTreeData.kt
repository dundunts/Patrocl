package org.turter.patrocl.domain.model.menu

data class CategoriesTreeData(
    val rootCategoryRkId: String,
    val categories: List<CategoryInfo>
)