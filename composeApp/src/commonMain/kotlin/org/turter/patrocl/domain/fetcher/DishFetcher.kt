package org.turter.patrocl.domain.fetcher

import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.menu.deprecated.Dish

interface DishFetcher: SourceFetcher<List<StationDishInfo>> {
}