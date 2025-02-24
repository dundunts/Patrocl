package org.turter.patrocl.data.remote.client

import org.turter.patrocl.data.dto.source.waiter.WaiterInfoDto

interface WaiterApiClient {
    suspend fun getOwnWaiterForUser(): Result<WaiterInfoDto>
    suspend fun getLoggedInWaitersInSameStation(): Result<List<WaiterInfoDto>>
}