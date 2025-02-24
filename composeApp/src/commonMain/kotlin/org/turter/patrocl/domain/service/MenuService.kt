package org.turter.patrocl.domain.service

import kotlinx.coroutines.flow.StateFlow
import org.turter.patrocl.domain.model.DataStatus
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.menu.deprecated.MenuData

interface MenuService {
    fun getMenuTreeDataStateFlow(): StateFlow<FetchState<MenuTreeData>>
    fun getMenuTreeDataStatusStateFlow(): StateFlow<DataStatus>
    suspend fun refreshMenu()
    suspend fun refreshMenuFromApi()
}