package org.turter.patrocl.data_mock

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.turter.patrocl.domain.model.DataVersionsActualizerStatus
import org.turter.patrocl.domain.service.DataVersionService

class DataVersionServiceMock : DataVersionService {
    private val _dataVersionStatus =
        MutableStateFlow<DataVersionsActualizerStatus>(DataVersionsActualizerStatus.Completed)

    override fun getStatus(): StateFlow<DataVersionsActualizerStatus> {
        return _dataVersionStatus.asStateFlow()
    }

    override fun actualizeAndGetStatus(): StateFlow<DataVersionsActualizerStatus> {
        return _dataVersionStatus.asStateFlow()
    }

    override suspend fun actualize() {
        _dataVersionStatus.value = DataVersionsActualizerStatus.Checking
        delay(300)
        _dataVersionStatus.value = DataVersionsActualizerStatus.Updating
        delay(300)
        _dataVersionStatus.value = DataVersionsActualizerStatus.Completed
    }
}