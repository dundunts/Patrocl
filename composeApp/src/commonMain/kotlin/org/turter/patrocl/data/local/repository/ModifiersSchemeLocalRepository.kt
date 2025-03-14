package org.turter.patrocl.data.local.repository

import org.turter.patrocl.data.local.CompanyLocalSource
import org.turter.patrocl.data.local.entity.menu.ModifiersSchemeLocal

interface ModifiersSchemeLocalRepository: CompanyLocalSource<List<ModifiersSchemeLocal>> {
    fun countDetails(): Long
}