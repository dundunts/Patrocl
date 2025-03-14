package org.turter.patrocl.data.local.repository.impl.prefs

import co.touchlab.kermit.Logger
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.turter.patrocl.data.local.getStringFlowOrEmpty

@OptIn(ExperimentalSettingsApi::class)
class SourceDataPrefsImpl(
    private val settings: Settings
) {
    companion object {
        const val ROOT_CATEGORY_RK_ID_KEY = "rootCategoryRkId"
        const val ROOT_MODIFIER_GROUP_RK_ID_KEY = "rootModifierGroupRkId"
        const val DEFAULT_HALL_RK_ID_KEY = "defaultHallRkId"
    }

    private val log = Logger.withTag("SourceDataPrefsImpl")

    private val flowSettings = (settings as ObservableSettings).toFlowSettings()

    fun getRootCategoryRkId(): Flow<String> {
        return flowSettings.getStringFlowOrEmpty(ROOT_CATEGORY_RK_ID_KEY)
    }

    suspend fun setRootCategoryRkId(value: String) {
        flowSettings.putString(ROOT_CATEGORY_RK_ID_KEY, value)
    }

    fun getRootModifierGroupRkId(): Flow<String> {
        return flowSettings.getStringFlowOrEmpty(ROOT_MODIFIER_GROUP_RK_ID_KEY)
    }

    suspend fun setRootModifierGroupRkId(value: String) {
        flowSettings.putString(ROOT_MODIFIER_GROUP_RK_ID_KEY, value)
    }

    fun getDefaultHallRkId(): Flow<String> {
        return flowSettings.getStringFlowOrEmpty(DEFAULT_HALL_RK_ID_KEY)
    }

    suspend fun setDefaultHallRkId(value: String) {
        flowSettings.putString(DEFAULT_HALL_RK_ID_KEY, value)
    }

    suspend fun clear() {
        flowSettings.remove(ROOT_CATEGORY_RK_ID_KEY)
        flowSettings.remove(ROOT_MODIFIER_GROUP_RK_ID_KEY)
        flowSettings.remove(DEFAULT_HALL_RK_ID_KEY)
    }
}