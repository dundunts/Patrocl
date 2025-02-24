package org.turter.patrocl.data.local.repository

import org.turter.patrocl.data.local.LocalSource
import org.turter.patrocl.data.local.entity.hall.HallLocal
import org.turter.patrocl.data.local.entity.menu.CategoryLocal

interface HallLocalRepository: LocalSource<List<HallLocal>> {
}