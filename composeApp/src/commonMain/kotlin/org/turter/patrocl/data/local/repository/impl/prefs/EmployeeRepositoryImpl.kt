package org.turter.patrocl.data.local.repository.impl.prefs

import co.touchlab.kermit.Logger
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import org.turter.patrocl.data.local.getStringFlowOrEmpty

@OptIn(ExperimentalSettingsApi::class)
class EmployeeRepositoryImpl(
    private val settings: Settings
) {
    companion object {
        const val EMPLOYEE_ID_KEY = "employeeId"
        const val PREFERRED_COMPANY_ID_KEY = "preferredCompanyId"
    }

    private val log = Logger.withTag("EmployeeRepositoryImpl")

    private val flowSettings = (settings as ObservableSettings).toFlowSettings()

    fun getEmployeeId(): Flow<String> {
        return flowSettings.getStringFlowOrEmpty(EMPLOYEE_ID_KEY)
    }

    suspend fun setEmployeeId(value: String) {
        flowSettings.putString(EMPLOYEE_ID_KEY, value)
    }

    fun getPreferredCompanyId(): Flow<String> {
        return flowSettings.getStringFlowOrEmpty(PREFERRED_COMPANY_ID_KEY)
    }

    suspend fun setPreferredCompanyId(value: String) {
        flowSettings.putString(PREFERRED_COMPANY_ID_KEY, value)
    }

    suspend fun clear() {
        flowSettings.remove(EMPLOYEE_ID_KEY)
        flowSettings.remove(PREFERRED_COMPANY_ID_KEY)
    }
}