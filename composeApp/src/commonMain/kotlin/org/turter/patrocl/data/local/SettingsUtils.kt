package org.turter.patrocl.data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
fun FlowSettings.getStringFlowOrEmpty(key: String): Flow<String> =
    this.getStringFlow(key, "")