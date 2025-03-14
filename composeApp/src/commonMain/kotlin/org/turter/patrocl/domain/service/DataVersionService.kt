package org.turter.patrocl.domain.service

import kotlinx.coroutines.flow.StateFlow
import org.turter.patrocl.domain.model.DataVersionsActualizerStatus

interface DataVersionService {

    fun actualizeAndGetStatus(): StateFlow<DataVersionsActualizerStatus>

    suspend fun actualize()

}