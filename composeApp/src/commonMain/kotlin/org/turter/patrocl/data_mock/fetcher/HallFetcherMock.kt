package org.turter.patrocl.data_mock.fetcher

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.turter.patrocl.data_mock.utils.TableDataSupplier
import org.turter.patrocl.domain.fetcher.HallFetcher
import org.turter.patrocl.domain.model.DataStatus
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.hall.HallsData

class HallFetcherMock : HallFetcher {
    private val DEFAULT_HALLS = TableDataSupplier.hallsData

    private val hallsState = FetchState.success(DEFAULT_HALLS)

    private val hallsStateFlow = MutableStateFlow<FetchState<HallsData>>(hallsState)

    private val dataStatusFlow = MutableStateFlow<DataStatus>(DataStatus.Ready)

    override fun getStateFlow(): StateFlow<FetchState<HallsData>> {
        return hallsStateFlow.asStateFlow()
    }

    override fun getDataStatus(): StateFlow<DataStatus> {
        return dataStatusFlow.asStateFlow()
    }

    override suspend fun refresh() {
        hallsStateFlow.value = FetchState.loading()
        dataStatusFlow.value = DataStatus.Loading
        delay(300)
        hallsStateFlow.value = hallsState
        dataStatusFlow.value = DataStatus.Ready
    }

    override suspend fun refreshFromRemote() {
        hallsStateFlow.value = FetchState.loading()
        dataStatusFlow.value = DataStatus.Loading
        delay(300)
        hallsStateFlow.value = hallsState
        dataStatusFlow.value = DataStatus.Ready
    }
}