package org.turter.patrocl.data.service

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.turter.patrocl.data.dto.order.request.UpdateOrderInfoPayload
import org.turter.patrocl.data.dto.order.response.OrdersListApiResponse
import org.turter.patrocl.data.mapper.order.toAddItemsPayload
import org.turter.patrocl.data.mapper.order.toCreateOrderPayload
import org.turter.patrocl.data.mapper.order.toOrder
import org.turter.patrocl.data.mapper.order.toOrderList
import org.turter.patrocl.data.mapper.order.toRemoveItemsFromOrderPayload
import org.turter.patrocl.data.remote.client.OrderApiClient
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.Message
import org.turter.patrocl.domain.model.hall.TableInfo
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.order.OrderPreview
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.domain.model.order.RemoveOrderItemsSession
import org.turter.patrocl.domain.service.MessageService
import org.turter.patrocl.domain.service.OrderService

class OrderServiceImpl(
    private val orderApiClient: OrderApiClient,
    private val messageService: MessageService
) : OrderService {
    private val log = Logger.withTag("OrderServiceImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val checkCurrentOrderFlow = MutableSharedFlow<OrderActuator>(replay = 1)

    private val checkOrdersStateEvent = MutableSharedFlow<Unit>(replay = 1)

    private val ordersStateFlow = channelFlow<FetchState<List<OrderPreview>>> {
        checkOrdersStateEvent.emit(Unit)

        var activeJob: Job? = null

        checkOrdersStateEvent.collect {
            send(FetchState.loading())

            activeJob?.cancel()

            activeJob = coroutineScope.launch {
                try {
                    orderApiClient.getActiveOrdersFlow().collect { result ->
                        val data = result.getOrThrow().let {
                            if (it.status == OrdersListApiResponse.Status.ERROR) {
                                Result.failure(RuntimeException(it.message))
                            } else {
                                Result.success(it.orders.toOrderList())
                            }
                        }
                        send(FetchState.done(data))
                        log.d { "Emit new orders flow: $data" }
                    }
                } catch (e: Exception) {
                    log.e { "Catch exception while collecting orders flow. Exception: $e" }
                    e.printStackTrace()
                    send(FetchState.fail(e))
                }
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    private val currentOrderFlow = MutableStateFlow<FetchState<Order>>(FetchState.initial())

    init {
        coroutineScope.launch {
            log.d { "Start observing currentOrderFlow" }
            checkCurrentOrderFlow.collect { actuator ->
                when (actuator) {
                    is OrderActuator.RefreshCurrent -> {
                        log.d { "Collect RefreshCurrent - check is current order existed" }
                        currentOrderFlow.value.takeIfSuccess()?.guid?.let { orderGuid ->
                            log.d { "Collect RefreshCurrent - current order is existed, " +
                                    "start fetching from remote" }
                            fetchOrderFromApi(orderGuid)
                        }
                    }

                    is OrderActuator.Load -> {
                        log.d { "Collect Load - start fetching from remote" }
                        fetchOrderFromApi(actuator.orderGuid)
                    }

                    is OrderActuator.CheckFor -> {
                        log.d { "Collect CheckFor - check is target equal to current order" }
                        currentOrderFlow.value.takeIfSuccess()?.guid?.let { orderGuid ->
                            if (actuator.orderGuid == orderGuid) {
                                log.d { "Collect CheckFor - target equals current, " +
                                        "start fetching from remote" }
                                fetchOrderFromApi(orderGuid)
                            }
                        }
                    }

                    is OrderActuator.Update -> {
                        log.d { "Collect Update - check is target equal to current order" }
                        val order = actuator.order
                        currentOrderFlow.value.takeIfSuccess()?.guid?.let { orderGuid ->
                            if (order.guid == orderGuid) {
                                log.d { "Collect Update - target equals current, " +
                                        "emit order value" }
                                currentOrderFlow.value = FetchState.success(order)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun openAndGetCurrentOrderFlow(guid: String): StateFlow<FetchState<Order>> {
        log.d { "Send event to load new current order with guid: $guid" }
        coroutineScope.launch { checkCurrentOrderFlow.emit(OrderActuator.Load(guid)) }
        log.d { "Return currentOrderFlow" }
        return currentOrderFlow.asStateFlow()
    }

    override fun getActiveOrdersStateFlow(): StateFlow<FetchState<List<OrderPreview>>> =
        ordersStateFlow

    override suspend fun refreshOrders() = checkOrdersStateEvent.emit(Unit)

    override suspend fun refreshCurrentOrder() {
        log.d { "Emit RefreshCurrent event to checkCurrentOrderFlow" }
        checkCurrentOrderFlow.emit(OrderActuator.RefreshCurrent)
    }

//    override suspend fun refreshCurrentOrder() = checkCurrentOrderFlow.emit(OrderActuator.Check)

    override suspend fun createOrder(
        table: TableInfo,
        waiter: Waiter,
        orderItems: List<NewOrderItem>
    ): Result<Order> {
        log.d { "Start creating order" }
        val result = orderApiClient.createOrder(
            payload = toCreateOrderPayload(
                tableCode = table.code,
                waiterCode = waiter.code,
                orderItems = orderItems
            )
        ).map { it.toOrder() }
        result.fold(
            onSuccess = { order ->
                log.d { "Order created: ${order.name}" }
                messageService.setMessage(Message.success("Order created: ${order.name}"))
            },
            onFailure = { cause ->
                log.e { "Catch exception while creating order: $cause" }
                cause.printStackTrace()
                messageService.setMessage(Message.error(cause))
            }
        )
        return result
    }

    override suspend fun addItemsToOrder(
        orderGuid: String,
        newItems: List<NewOrderItem>
    ): Result<Order> {
        log.d { "Start updating order" }
        val result = orderApiClient
            .updateOrder(payload = newItems.toAddItemsPayload(orderGuid = orderGuid))
            .map { it.toOrder() }
        result.fold(
            onSuccess = { order ->
                log.d { "Order updated: ${order.name}" }
                checkCurrentOrderFlow.emit(OrderActuator.Update(order))
                messageService.setMessage(Message.success("Order updated: ${order.name}"))
            },
            onFailure = { cause ->
                log.e { "Catch exception while creating order: $cause" }
                cause.printStackTrace()
                messageService.setMessage(Message.error(cause))
            }
        )
        return result
    }

    override suspend fun removeItemFromOrderSession(
        orderGuid: String,
        payload: RemoveOrderItemsSession
    ): Result<Order> {
        log.d { "Start removing items: ${payload.itemsForRemove.map { it.name }}" }
        val result = orderApiClient
            .removeItem(payload = payload.toRemoveItemsFromOrderPayload(orderGuid))
            .map { it.toOrder() }
        result.fold(
            onSuccess = { order ->
                log.d { "Success removing items: ${payload.itemsForRemove.map { it.name }}" }
                checkCurrentOrderFlow.emit(OrderActuator.Update(order))
                messageService.setMessage(
                    Message.success(
                        "Removing item: ${payload.itemsForRemove.count()} is successful"
                    )
                )
            },
            onFailure = { cause ->
                log.e {
                    "Failure removing items: ${payload.itemsForRemove.count()}. " +
                            "Exception: $cause"
                }
                cause.printStackTrace()
                messageService.setMessage(Message.error(cause))
            }
        )
        return result
    }

    override suspend fun removeItemsFromOrderSessions(
        orderGuid: String,
        payload: List<RemoveOrderItemsSession>
    ): Result<Order> {
        log.d {
            "Start removing items: " +
                    "${payload.flatMap { it.itemsForRemove.map { dish -> dish.name } }}"
        }
        val result = orderApiClient
            .removeItem(payload = payload.toRemoveItemsFromOrderPayload(orderGuid))
            .map { it.toOrder() }
        result.fold(
            onSuccess = { order ->
                log.d {
                    "Success removing items: " +
                            "${payload.flatMap { it.itemsForRemove.map { dish -> dish.name } }}"
                }
                checkCurrentOrderFlow.emit(OrderActuator.Update(order))
                messageService.setMessage(
                    Message.success(
                        "Removing item: " +
                                "${payload.flatMap { it.itemsForRemove }.count()} is successful"
                    )
                )
            },
            onFailure = { cause ->
                log.e {
                    "Failure removing items: ${payload.flatMap { it.itemsForRemove }.count()}. " +
                            "Exception: $cause"
                }
                cause.printStackTrace()
                messageService.setMessage(Message.error(cause))
            }
        )
        return result
    }

    override suspend fun updateOrderInfo(
        orderGuid: String,
        waiter: Waiter,
        table: TableInfo
    ): Result<Unit> {
        log.d {
            "Start updating info(waiter = $waiter, table = $table) " +
                    "for order with guid: $orderGuid"
        }
        return orderApiClient.updateOrderInfo(
            UpdateOrderInfoPayload(
                orderGuid = orderGuid,
                waiterCode = waiter.code,
                tableCode = table.code
            )
        ).onSuccess {
            log.d { "Successful updating order info for order guid: $orderGuid" }
            log.d { "Emit update info order actuator" }
            checkCurrentOrderFlow.emit(OrderActuator.CheckFor(orderGuid))
            messageService.setMessage(Message.success("Success updating order info"))
        }.onFailure { cause ->
            log.e { "Fail to update info for order guid: $orderGuid, cause: $cause" }
            cause.printStackTrace()
            messageService.setMessage(Message.error(cause))
        }
    }

    private suspend fun fetchOrderFromApi(guid: String) {
        currentOrderFlow.emit(FetchState.loading())
        log.d { "Collect checkCurrentOrderFlow for order: $guid" }

        val result = orderApiClient.getOrderByGuid(guid).map { it.toOrder() }

        result.onSuccess { order ->
            log.d { "Get order by guid from api. Order name: ${order.name}" }
        }.onFailure { cause ->
            log.e { "Catch exception while getting order by guid: $cause" }
            messageService.setMessage(Message.error(cause))
        }

        log.d { "Emit result: $result" }
        currentOrderFlow.emit(FetchState.done(result))
    }

    private sealed class OrderActuator {
        data object RefreshCurrent : OrderActuator()
        data class Load(val orderGuid: String) : OrderActuator()
        data class CheckFor(val orderGuid: String) : OrderActuator()
        data class Update(val order: Order) : OrderActuator()
    }
}