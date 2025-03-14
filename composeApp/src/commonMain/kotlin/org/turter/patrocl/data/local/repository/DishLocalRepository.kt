package org.turter.patrocl.data.local.repository

import org.turter.patrocl.data.local.CompanyLocalSource
import org.turter.patrocl.data.local.LocalSource
import org.turter.patrocl.data.local.entity.menu.DishLocal

interface DishLocalRepository : CompanyLocalSource<List<DishLocal>> {
}