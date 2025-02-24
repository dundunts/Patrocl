package org.turter.patrocl.domain.fetcher

import org.turter.patrocl.domain.model.menu.ModifierGroupInfo
import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo
import org.turter.patrocl.domain.model.menu.deprecated.ModifiersGroup

interface ModifiersSchemeFetcher: SourceFetcher<List<ModifierSchemeInfo>> {
}