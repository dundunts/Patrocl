package org.turter.patrocl.data.remote.client.impl

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import org.turter.patrocl.data.dto.stoplist.CreateStopListItemPayload
import org.turter.patrocl.data.dto.stoplist.EditStopListItemPayload
import org.turter.patrocl.data.dto.stoplist.StopListDto
import org.turter.patrocl.data.dto.stoplist.StopListItemDto
import org.turter.patrocl.data.remote.client.ApiEndpoint
import org.turter.patrocl.data.remote.client.StopListApiClient
import org.turter.patrocl.data.remote.client.proceedRequest

class StopListApiClientImpl(
    private val httpClient: HttpClient
) : StopListApiClient {
    private val log = Logger.withTag("StopListApiClientImpl")

    override suspend fun getStopList(): Result<List<StopListItemDto>> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.StopList.getStopList()) },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun getStopListFlow(): Flow<Result<StopListDto>> {
        return callbackFlow {
            log.d { "Start stop list flow sse" }
//            var reconnectionCount = 0
//
//            while (maxReconnections < 0 || reconnectionCount <= maxReconnections) {
//                if (reconnectionCount == 0) log.d { "Start try to connect" }
//                else log.d { "Try reconnect: current tries = $reconnectionCount, " +
//                        "max tries = $maxReconnections" }
                try {
                    httpClient.sse(ApiEndpoint.StopList.getStopListFlow()) {
                        incoming.collect { event ->
                            log.d(event.toString())
                            val data = event.data
                            if (!data.isNullOrBlank()) {
                                try {
                                    val stopList: StopListDto = Json.decodeFromString(data)
                                    log.d { "Decoded stop list response status: ${stopList.status}" }
                                    if (stopList.status != StopListDto.Status.PING) {
                                        trySend(Result.success(stopList))
                                    }
                                } catch (e: Exception) {
                                    trySend(Result.failure(e))
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
//                    when (e) {
//                        is SSEClientException -> {
//                            log.e { "Catch exception in sse: ${e.message}" }
//                            if (maxReconnections >= 0 && ++reconnectionCount > maxReconnections) close()
//                        }
//
//                        else -> {
                            trySend(Result.failure(e))
                            close()
//                        }
//                    }
//                }
            }

            awaitClose {
                log.d { "Stop list sse flow canceled by await close" }
                cancel()
            }
        }
    }

    override suspend fun createItem(payload: CreateStopListItemPayload): Result<StopListItemDto> =
        proceedRequest(
            action = {
                httpClient.post(ApiEndpoint.StopList.createStopListItem()) {
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }
            },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun editItem(payload: EditStopListItemPayload): Result<StopListItemDto> =
        proceedRequest(
            action = {
                httpClient.post(ApiEndpoint.StopList.editStopListItem()) {
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }
            },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun deleteItem(itemId: String): Result<Unit> =
        proceedRequest(
            action = { httpClient.delete(ApiEndpoint.StopList.removeStopListItem(itemId)) },
            decoder = { }
        )

    override suspend fun deleteItems(ids: List<String>): Result<Unit> {
        return proceedRequest(
            action = {
                httpClient.post(ApiEndpoint.StopList.removeStopListItems()) {
                    contentType(ContentType.Application.Json)
                    setBody(ids)
                }
            },
            decoder = { }
        )
    }
}