package org.turter.patrocl.domain.fetcher

import org.turter.patrocl.domain.model.menu.StationModifierInfo
import org.turter.patrocl.domain.model.menu.deprecated.DishModifier

interface ModifiersFetcher: SourceFetcher<List<StationModifierInfo>> {
}