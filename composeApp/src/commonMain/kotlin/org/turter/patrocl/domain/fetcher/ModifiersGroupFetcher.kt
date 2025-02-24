package org.turter.patrocl.domain.fetcher

import org.turter.patrocl.domain.model.menu.ModifierGroupInfo
import org.turter.patrocl.domain.model.menu.ModifiersGroupsTreeData
import org.turter.patrocl.domain.model.menu.deprecated.ModifiersGroup

interface ModifiersGroupFetcher: SourceFetcher<ModifiersGroupsTreeData> {
}