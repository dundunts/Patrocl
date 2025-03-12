package org.turter.patrocl.data.remote.client.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.turter.patrocl.data.dto.source.voids.CompanyOrderItemVoidsData
import org.turter.patrocl.data.remote.client.ApiEndpoint
import org.turter.patrocl.data.remote.client.OrderItemVoidApiClient
import org.turter.patrocl.data.remote.client.proceedRequest

class OrderItemVoidApiClientImpl(private val httpClient: HttpClient) : OrderItemVoidApiClient {
    override suspend fun getOrderItemVoidsForUser(): Result<CompanyOrderItemVoidsData> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.OrderItemVoids.getOrderItemVoidsData()) },
            decoder = { Json.decodeFromString(it.body()) }
        )
}