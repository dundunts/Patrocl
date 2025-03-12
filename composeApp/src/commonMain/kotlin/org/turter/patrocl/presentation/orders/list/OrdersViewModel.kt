package org.turter.patrocl.presentation.orders.list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.person.StationWaiterStatus
import org.turter.patrocl.domain.service.OrderService
import org.turter.patrocl.domain.service.WaiterService
import org.turter.patrocl.presentation.error.ErrorType

sealed class OrdersUiEvent {
    data object RefreshOrders : OrdersUiEvent()
    data object CheckWaiterLoggedInOnStation : OrdersUiEvent()
    data class SetFilter(val filter: OrdersFilter) : OrdersUiEvent()
}

class OrdersViewModel(
    private val orderService: OrderService,
    private val waiterService: WaiterService
) : ScreenModel {

    private val coroutineScope = screenModelScope

    private val _screenState = MutableStateFlow<OrdersScreenState>(OrdersScreenState.Initial)

    val screenState: StateFlow<OrdersScreenState> = _screenState

    init {
        observeData()
    }

    private fun observeData() {
        coroutineScope.launch {
            combine(
                orderService.getActiveOrdersStateFlow(),
                waiterService.getOwnWaiterStateFlow(),
                waiterService.getStationOwnWaiterStatus()
            ) { fetchedOrders, fetchedWaiter, fetchedWaiterStatus ->
                when (fetchedWaiterStatus) {
                    is StationWaiterStatus.LoggedIn -> {
                        if (fetchedOrders is FetchState.Finished
                            && fetchedWaiter is FetchState.Finished
                        ) {
                            try {
                                val orders = fetchedOrders.result.getOrThrow().sortedBy { it.name }
                                val waiter = fetchedWaiter.result.getOrThrow()
                                when (val currentState = _screenState.value) {
                                    is OrdersScreenState.Content -> currentState.copy(
                                        orders = orders,
                                        waiter = waiter
                                    )

                                    else -> OrdersScreenState.Content(
                                        orders = orders,
                                        waiter = waiter,
                                        filter = OrdersFilter()
                                    )
                                }
                            } catch (e: Exception) {
                                OrdersScreenState.Error(errorType = ErrorType.from(e))
                            }
                        } else {
                            OrdersScreenState.Loading
                        }
                    }

                    is StationWaiterStatus.NotLoggedIn -> OrdersScreenState.NotLoggedInOnStation

                    is StationWaiterStatus.Error -> {
                        OrdersScreenState.Error(ErrorType.from(fetchedWaiterStatus.causes.first()))
                    }

                    else -> OrdersScreenState.Loading
                }

            }.collect { newState ->
                _screenState.value = newState
            }
        }
    }

    fun sendEvent(event: OrdersUiEvent) {
        when (event) {
            is OrdersUiEvent.RefreshOrders -> refreshData()
            is OrdersUiEvent.CheckWaiterLoggedInOnStation -> checkLoggedInWaiterOnStation()
            is OrdersUiEvent.SetFilter -> setFilter(event.filter)
        }
    }

    private fun checkLoggedInWaiterOnStation() {
        coroutineScope.launch {
            waiterService.checkStationWaiterStatus()
        }
    }

    private fun refreshData() = coroutineScope.launch {
        orderService.refreshOrders()
        waiterService.checkWaiter()
        waiterService.checkLoggedInWaitersInSameStation()
    }

    private fun setFilter(filter: OrdersFilter) {
        transformMainState { it.copy(filter = filter) }
    }

    private fun transformMainState(
        action: (state: OrdersScreenState.Content) -> OrdersScreenState
    ) {
        val currentState = _screenState.value
        if (currentState is OrdersScreenState.Content) {
            _screenState.value = action(currentState)
        }
    }

    private fun withMainState(): OrdersScreenState.Content? {
        val state = _screenState.value
        return if (state is OrdersScreenState.Content) state else null
    }
}