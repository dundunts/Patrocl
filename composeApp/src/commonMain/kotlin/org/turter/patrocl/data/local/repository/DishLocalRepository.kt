package org.turter.patrocl.data.local.repository

import org.turter.patrocl.data.local.LocalSource
import org.turter.patrocl.data.local.entity.menu.DishLocal

interface DishLocalRepository : LocalSource<List<DishLocal>> {
}