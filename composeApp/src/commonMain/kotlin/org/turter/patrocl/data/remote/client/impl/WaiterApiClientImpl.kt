package org.turter.patrocl.data.remote.client.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import org.turter.patrocl.data.dto.source.deprecated.WaiterDto
import org.turter.patrocl.data.dto.source.waiter.WaiterInfoDto
import org.turter.patrocl.data.remote.client.ApiEndpoint
import org.turter.patrocl.data.remote.client.WaiterApiClient
import org.turter.patrocl.data.remote.client.proceedRequest

class WaiterApiClientImpl(
    private val httpClient: HttpClient
): WaiterApiClient {
    override suspend fun getOwnWaiterForUser(): Result<WaiterInfoDto> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Waiter.getOwnWaiter()) },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun getLoggedInWaitersInSameStation(): Result<List<WaiterInfoDto>> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Waiter.getLoggedInWaitersInSameStation()) },
            decoder = { Json.decodeFromString(it.body()) }
        )
}