package org.turter.patrocl.data.remote.client.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import org.turter.patrocl.data.dto.source.dataversion.DataVersionDto
import org.turter.patrocl.data.remote.client.ApiEndpoint
import org.turter.patrocl.data.remote.client.DataVersionApiClient
import org.turter.patrocl.data.remote.client.proceedRequest

class DataVersionApiClientImpl(
    private val httpClient: HttpClient
) : DataVersionApiClient {
    override suspend fun getDataVersionsForCompany(companyId: String): Result<List<DataVersionDto>> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.DataVersion.getDataVersionsForCompany(companyId)) },
            decoder = { Json.decodeFromString(it.body()) }
        )
}