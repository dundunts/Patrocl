package org.turter.patrocl.data.remote.client.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import org.turter.patrocl.data.dto.source.hall.CompanyHallsData
import org.turter.patrocl.data.remote.client.ApiEndpoint
import org.turter.patrocl.data.remote.client.HallApiClient
import org.turter.patrocl.data.remote.client.proceedRequest

class HallApiClientImpl(
    private val httpClient: HttpClient
): HallApiClient {
    override suspend fun getHallsForUser(): Result<CompanyHallsData> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Hall.getHallsData()) },
            decoder = { Json.decodeFromString(it.body()) }
        )
}