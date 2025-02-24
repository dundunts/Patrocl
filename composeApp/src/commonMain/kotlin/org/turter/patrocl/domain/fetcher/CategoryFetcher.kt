package org.turter.patrocl.domain.fetcher

import org.turter.patrocl.domain.model.menu.CategoriesTreeData
import org.turter.patrocl.domain.model.menu.CategoryInfo
import org.turter.patrocl.domain.model.menu.deprecated.Category

interface CategoryFetcher: SourceFetcher<CategoriesTreeData> {
}