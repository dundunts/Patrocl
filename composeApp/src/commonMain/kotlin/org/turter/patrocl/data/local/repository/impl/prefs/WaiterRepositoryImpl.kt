package org.turter.patrocl.data.local.repository.impl.prefs

import co.touchlab.kermit.Logger
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.turter.patrocl.data.local.getStringFlowOrEmpty
import org.turter.patrocl.domain.exception.EmptyLocalDataException
import org.turter.patrocl.domain.model.person.Waiter

@OptIn(ExperimentalSettingsApi::class)
class WaiterRepositoryImpl(
    private val settings: Settings
) {
    companion object {
        const val WAITER_ID_KEY = "waiterId"
        const val WAITER_RK_ID_KEY = "waiterRkId"
        const val WAITER_GUID_KEY = "waiterGuid"
        const val WAITER_NAME_KEY = "waiterName"
        const val WAITER_CODE_KEY = "waiterCode"
    }

    private val log = Logger.withTag("PersonRepositoryImpl")

    private val flowSettings = (settings as ObservableSettings).toFlowSettings()

    fun getWaiter(): Flow<Result<Waiter>> {
        return combine(
            flowSettings.getStringFlowOrEmpty(WAITER_ID_KEY),
            flowSettings.getStringFlowOrEmpty(WAITER_RK_ID_KEY),
            flowSettings.getStringFlowOrEmpty(WAITER_GUID_KEY),
            flowSettings.getStringFlowOrEmpty(WAITER_NAME_KEY),
            flowSettings.getStringFlowOrEmpty(WAITER_CODE_KEY)
        ) { id, rkId, guid, name, code ->
            log.d { "Combine waiter props to waiter. " +
                    "Id: $id, rkId: $rkId, guid: $guid, name: $name, code: $code" }
            if (id.isNotBlank() && rkId.isNotBlank() && guid.isNotBlank() && code.isNotBlank()) {
                log.d { "Props aren`t blank - build success waiter result" }
                Result.success(Waiter(
                    id = id,
                    rkId = rkId,
                    guid = guid,
                    code = code,
                    name = name
                ))
            } else {
                log.d { "Some props are blank - build failure waiter result" }
                Result.failure(EmptyLocalDataException())
            }
        }
    }

    suspend fun setWaiter(waiter: Waiter) {
        log.d { "Start set waiter props to prefs. Waiter: $waiter" }
        flowSettings.putString(WAITER_ID_KEY, waiter.id)
        flowSettings.putString(WAITER_RK_ID_KEY, waiter.rkId)
        flowSettings.putString(WAITER_GUID_KEY, waiter.guid)
        flowSettings.putString(WAITER_NAME_KEY, waiter.name)
        flowSettings.putString(WAITER_CODE_KEY, waiter.code)
        log.d { "Complete set waiter props to prefs." }
    }

    suspend fun clear() {
        log.d { "Start clear waiter prefs" }
        flowSettings.remove(WAITER_ID_KEY)
        flowSettings.remove(WAITER_RK_ID_KEY)
        flowSettings.remove(WAITER_GUID_KEY)
        flowSettings.remove(WAITER_NAME_KEY)
        flowSettings.remove(WAITER_CODE_KEY)
        log.d { "Complete clear waiter prefs" }
    }

}