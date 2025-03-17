package org.turter.patrocl.domain.service

import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.stoplist.NewStopListItem
import org.turter.patrocl.domain.model.stoplist.StopList

interface StopListService {

    fun getStopListStateFlow(): StateFlow<FetchState<StopList>>

    suspend fun refreshStopList()

    suspend fun createNewItem(item: NewStopListItem): Result<Unit>

    suspend fun editItem(rkId: String, remainingCount: Int, until: LocalDateTime?): Result<Unit>

    suspend fun removeItem(id: String): Result<Unit>

    suspend fun removeItems(ids: List<String>): Result<Unit>

}