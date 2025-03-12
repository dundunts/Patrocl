package org.turter.patrocl.presentation.orders.create

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.turter.patrocl.domain.fetcher.HallFetcher
import org.turter.patrocl.domain.model.FetchState.Finished
import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.domain.service.MenuService
import org.turter.patrocl.domain.service.OrderService
import org.turter.patrocl.domain.service.WaiterService
import org.turter.patrocl.presentation.error.ErrorType
import org.turter.patrocl.presentation.orders.common.CommonOrderEventProcessor
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent
import org.turter.patrocl.presentation.orders.common.OrderInfo
import org.turter.patrocl.presentation.orders.create.test.testNewItems

sealed class CreateOrderUiEvent {
    data object RefreshData : CreateOrderUiEvent()
    data object CreateOrderAndContinue : CreateOrderUiEvent()
    data object CreateOrderAndFinish : CreateOrderUiEvent()
}

class CreateOrderViewModel(
    private val menuService: MenuService,
    private val hallFetcher: HallFetcher,
    private val waiterService: WaiterService,
    private val orderService: OrderService,
    private val navigateToModifiersSelector: (
        item: NewOrderItem,
        menuData: MenuTreeData,
        autoOpened: Boolean,
        onSave: (item: NewOrderItem) -> Unit
    ) -> Unit,
    private val navigateToUpdateOrderInfoScreen: (
        info: OrderInfo,
        halls: HallsData,
        availableWaiters: List<Waiter>,
        onSave: (info: OrderInfo) -> Result<Unit>
    ) -> Unit,
    private val navigateToOrder: (orderGuid: String) -> Unit,
    private val navigateBack: () -> Unit
) : ScreenModel {
    private val log = Logger.withTag("CreateOrderViewModel")

    private val coroutineScope = screenModelScope

    private val processor = CommonOrderEventProcessor<CreateOrderScreenState.Main> (
        transformMainState = { function ->
            transformMainState(function)
        },
        withMainState = { withMainState() },
        navigateToModifiersSelector = navigateToModifiersSelector,
        navigateToUpdateOrderInfoScreen = navigateToUpdateOrderInfoScreen,
        onUpdateOrderInfo = {
            transformMainState { state -> state.copyCommon(orderInfo = it) }
                .let { Result.success(Unit) }
        },
        navigateBack = navigateBack
    )

    private val _screenState =
        MutableStateFlow<CreateOrderScreenState>(CreateOrderScreenState.Initial)

    val screenState = _screenState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        coroutineScope.launch {
            combine(
                menuService.getMenuTreeDataStateFlow(),
                hallFetcher.getStateFlow(),
                waiterService.getOwnWaiterStateFlow(),
                waiterService.getLoggedInWaitersInSameStation()
            ) { menuFetch, hallsFetch, waiterFetch, loggedInWaitersFetch ->
                log.d {
                    "Combine flows:\n " +
                            "-Menu: $menuFetch \n" +
                            "-Halls: $hallsFetch \n" +
                            "-Waiter: $waiterFetch" +
                            "-LoggedIn waiters: $loggedInWaitersFetch"
                }
                if (menuFetch is Finished
                    && hallsFetch is Finished
                    && waiterFetch is Finished
                    && loggedInWaitersFetch is Finished
                ) {
                    try {
                        val menuData = menuFetch.result.getOrThrow()
                        val halls = hallsFetch.result.getOrThrow()
                        val ownWaiter = waiterFetch.result.getOrThrow()
                        val waiters = loggedInWaitersFetch.result.getOrThrow()

//                        val rootCategory = menuData.categoryRkIdMap[menuData.rootCategoryRkId]
//                        log.d { "Root category: $rootCategory" }

                        val rootModisGroup = menuData.modifiersGroupRkIdMap[menuData.rootModifierGroupRkId]
                        log.d { "Root category: $rootModisGroup" }

                        when (val currentState = _screenState.value) {
                            is CreateOrderScreenState.Main -> currentState.copy(
                                menu = menuData,
                                halls = halls,
                                ownWaiter = ownWaiter,
                                waiters = waiters,
//                                menuState = currentState.menuState.copy(
//                                    currentCategory = currentState.menuState.currentCategory
//                                        ?:menuData.categoryRkIdMap[menuData.rootCategoryRkId]
//                                )
                            )

                            else -> CreateOrderScreenState.Main(
                                menu = menuData,
                                halls = halls,
                                ownWaiter = ownWaiter,
                                waiters = waiters,
                                orderInfo = OrderInfo(waiter = ownWaiter, table = null),
                                //TODO delete
//                                newOrderItems = testNewItems
                            )
                        }
                    } catch (e: Exception) {
                        log.e { "Catch exception in combine flows: $e" }
                        e.printStackTrace()
                        CreateOrderScreenState.Error(errorType = ErrorType.from(e))
                    }
                } else {
                    CreateOrderScreenState.Loading
                }
            }.collect { newScreenState: CreateOrderScreenState ->
                _screenState.value = newScreenState
            }
        }
    }

    fun sendEvent(event: CommonOrderUiEvent) {
        processor.processEvent(event)
    }

    fun sendEvent(event: CreateOrderUiEvent) {
        when(event) {
            is CreateOrderUiEvent.RefreshData -> refreshData()
            is CreateOrderUiEvent.CreateOrderAndContinue -> createOrder { navigateToOrder(it.guid) }
            is CreateOrderUiEvent.CreateOrderAndFinish -> createOrder { navigateBack() }
        }
    }

    private fun createOrder(onSuccess: (Order) -> Unit) {
        withMainState()?.apply {
            orderInfo.table?.let { table ->
                coroutineScope.launch {
                    setLoading(true)
                    orderService.createOrder(
                        table = table,
                        waiter = orderInfo.waiter,
                        orderItems = newOrderItems
                    ).onSuccess(onSuccess)
                    setLoading(false)
                }
            } ?: processor.processEvent(CommonOrderUiEvent.OpenUpdateOrderInfo)
        }
    }

    private fun refreshData() {
        _screenState.value = CreateOrderScreenState.Loading
        coroutineScope.launch {
            menuService.refreshMenu()
            hallFetcher.refresh()
        }
    }

    private fun setLoading(value: Boolean) {
        log.d { "Set isSaving to $value" }
        transformMainState { it.copy(isSaving = value) }
    }

    private fun transformMainState(
        action: (state: CreateOrderScreenState.Main) -> CreateOrderScreenState
    ) {
        val currentState = _screenState.value
        if (currentState is CreateOrderScreenState.Main) {
            _screenState.value = action(currentState)
        }
    }

    private fun withMainState(): CreateOrderScreenState.Main? {
        val state = _screenState.value
        return if (state is CreateOrderScreenState.Main) state else null
    }
}

