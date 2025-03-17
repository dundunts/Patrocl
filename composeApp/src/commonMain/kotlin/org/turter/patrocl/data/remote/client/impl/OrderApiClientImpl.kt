package org.turter.patrocl.data.remote.client.impl

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.sse.sse
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
import org.turter.patrocl.data.dto.order.request.CreateOrderPayload
import org.turter.patrocl.data.dto.order.request.OrderSessionPayload
import org.turter.patrocl.data.dto.order.request.RemoveItemsFromOrderPayload
import org.turter.patrocl.data.dto.order.request.UpdateOrderInfoPayload
import org.turter.patrocl.data.dto.order.response.OrderDto
import org.turter.patrocl.data.dto.order.response.OrdersListApiResponse
import org.turter.patrocl.data.remote.client.ApiEndpoint
import org.turter.patrocl.data.remote.client.OrderApiClient
import org.turter.patrocl.data.remote.client.proceedRequest
import org.turter.patrocl.domain.model.order.OrderPreview

class OrderApiClientImpl(
    private val httpClient: HttpClient
) : OrderApiClient {
    private val log = Logger.withTag("OrderApiClientImpl")

    override suspend fun getOrderByGuid(guid: String): Result<OrderDto> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Order.getOrder(guid)) },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun getActiveOrders(): Result<List<OrderPreview>> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Order.getOpenedOrdersList()) },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun getActiveOrdersFlow(): Flow<Result<OrdersListApiResponse>> {
        return callbackFlow {
            log.d { "Start orders flow sse" }

//            var reconnectionCount = 0
//
//            while (maxReconnectionTries < 0 || reconnectionCount <= maxReconnectionTries) {
//                if (reconnectionCount == 0) log.d { "Start try to connect" }
//                else {
//                    delay(500)
//                    log.d {
//                        "Try reconnect: current tries = $reconnectionCount, " +
//                                "max tries = $maxReconnectionTries"
//                    }
//                }
                try {
                    httpClient.sse(ApiEndpoint.Order.getOpenedOrdersListFlow()) {
                        incoming.collect { event ->
                            log.d(event.toString())
                            val data = event.data
                            if (!data.isNullOrBlank()) {
                                try {
                                    val ordersList: OrdersListApiResponse = Json.decodeFromString(data)
                                    log.d { "Decoded orders list response status: ${ordersList.status}" }
                                    if (ordersList.status != OrdersListApiResponse.Status.PING) {
                                        trySend(Result.success(ordersList))
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
//                            if (maxReconnectionTries >= 0 && ++reconnectionCount > maxReconnectionTries) {
//                                trySend(Result.failure(e))
//                                close()
//                            }
//                        }
//
//                        else -> {
                            trySend(Result.failure(e))
                            close()
//                        }
//                    }
                }
//            }

            awaitClose {
                log.d { "Orders sse flow canceled by await close" }
                cancel()
            }
        }
    }

    override suspend fun createOrder(payload: CreateOrderPayload): Result<OrderDto> =
        proceedRequest(
            action = {
                httpClient.post(ApiEndpoint.Order.createOrder()) {
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }
            },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun updateOrder(payload: OrderSessionPayload.AddDishes): Result<OrderDto> =
        proceedRequest(
            action = {
                httpClient.post(ApiEndpoint.Order.addItemsToOrder()) {
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }
            },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun removeItem(payload: RemoveItemsFromOrderPayload): Result<OrderDto> =
        proceedRequest(
            action = {
                httpClient.post(ApiEndpoint.Order.removeItemsFromOrder()) {
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }
            },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun updateOrderInfo(payload: UpdateOrderInfoPayload): Result<Unit> =
        proceedRequest(
            action = {
                httpClient.post(ApiEndpoint.Order.updateOrderInfo()) {
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }
            },
            decoder = { }
        )
}