package org.turter.patrocl.domain.fetcher

import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo

interface ModifiersSchemeFetcher: SourceFetcher<List<ModifierSchemeInfo>> {
    fun getActualDetailsCount(): Long
}