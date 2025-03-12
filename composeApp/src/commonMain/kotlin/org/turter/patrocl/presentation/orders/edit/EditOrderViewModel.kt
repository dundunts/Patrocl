package org.turter.patrocl.presentation.orders.edit

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
import org.turter.patrocl.domain.model.hall.TableInfo
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.domain.service.MenuService
import org.turter.patrocl.domain.service.OrderService
import org.turter.patrocl.domain.service.WaiterService
import org.turter.patrocl.presentation.error.ErrorType
import org.turter.patrocl.presentation.mapper.buildRemoveSession
import org.turter.patrocl.presentation.orders.common.CommonOrderEventProcessor
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent
import org.turter.patrocl.presentation.orders.common.OrderInfo
import org.turter.patrocl.presentation.orders.common.Selected
import org.turter.patrocl.presentation.orders.common.SelectedSavedDish
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.RefreshData
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.RemoveAllSelectedSavedItems
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.RemoveSelectedSavedItem
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.SaveOrderAndFinish
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.SaveOrderAndContinue
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.UnselectAllSavedSessionItems
import org.turter.patrocl.utils.mutate

sealed class EditOrderUiEvent {
    data class SelectSavedItem(val session: Order.Session, val item: Order.Dish) :
        EditOrderUiEvent()

    data class UnselectSavedItem(val session: Order.Session, val item: Order.Dish) :
        EditOrderUiEvent()

    data class SelectAllSavedSessionItems(val session: Order.Session) : EditOrderUiEvent()
    data class UnselectAllSavedSessionItems(val session: Order.Session) : EditOrderUiEvent()
    data object UnselectAllSavedItems : EditOrderUiEvent()

    data object OpenVoidSelectorForSelectedItems : EditOrderUiEvent()
    data class OpenVoidSelectorForSessionItem(val session: Order.Session, val item: Order.Dish) :
        EditOrderUiEvent()

    data object CloseVoidSelector : EditOrderUiEvent()

    data class RemoveSessionItem(
        val session: Order.Session,
        val item: Order.Dish,
        val rkQuantity: Int,
        val voidRkId: String
    ) : EditOrderUiEvent()

    data class RemoveSelectedSavedItem(val rkQuantity: Int, val voidRkId: String) :
        EditOrderUiEvent()

    data class RemoveAllSelectedSavedItems(val voidRkId: String) : EditOrderUiEvent()

    data object RefreshData : EditOrderUiEvent()
    data object SaveOrderAndContinue : EditOrderUiEvent()
    data object SaveOrderAndFinish : EditOrderUiEvent()
}

class EditOrderViewModel(
    private val orderGuid: String,
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
    private val navigateBack: () -> Unit
) : ScreenModel {
    private val log = Logger.withTag("EditOrderViewModel")

    private val coroutineScope = screenModelScope

    private val processor = CommonOrderEventProcessor<EditOrderScreenState.Main>(
        transformMainState = { function ->
            transformMainState(function)
        },
        withMainState = { withMainState() },
        navigateToModifiersSelector = navigateToModifiersSelector,
        navigateToUpdateOrderInfoScreen = navigateToUpdateOrderInfoScreen,
        onUpdateOrderInfo = { updateOrderInfo(it) },
        navigateBack = navigateBack
    )

    private val _screenState = MutableStateFlow<EditOrderScreenState>(EditOrderScreenState.Initial)

    val screenState = _screenState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        coroutineScope.launch {
            combine(
                orderService.getOrderFlow(orderGuid),
                menuService.getMenuTreeDataStateFlow(),
                hallFetcher.getStateFlow(),
                waiterService.getOwnWaiterStateFlow(),
                waiterService.getLoggedInWaitersInSameStation()
            ) { orderFetch, menuFetch, hallsFetch, waiterFetch, loggedInWaitersFetch ->
                log.d {
                    "Combine flows:\n " +
                            "-Order: $orderFetch \n" +
                            "-Menu: $menuFetch \n" +
                            "-Halls: $hallsFetch \n" +
                            "-Waiter: $waiterFetch" +
                            "-LoggedIn waiters: $loggedInWaitersFetch"
                }
                if (orderFetch is Finished
                    && menuFetch is Finished
                    && hallsFetch is Finished
                    && waiterFetch is Finished
                    && loggedInWaitersFetch is Finished
                ) {
                    try {
                        val orderData = orderFetch.result.getOrThrow()
                        val menuData = menuFetch.result.getOrThrow()
                        val halls = hallsFetch.result.getOrThrow()
                        val ownWaiter = waiterFetch.result.getOrThrow()
                        val waiters = loggedInWaitersFetch.result.getOrThrow()

                        val orderWaiter = orderData.waiter
                        val orderTable = orderData.table

                        log.d { "Order: $orderData" }

                        when (val currentState = _screenState.value) {
                            is EditOrderScreenState.Main -> currentState.copy(
                                order = orderData,
                                menu = menuData,
                                halls = halls,
                                ownWaiter = ownWaiter,
                                waiters = waiters
                            )

                            else -> EditOrderScreenState.Main(
                                order = orderData,
                                menu = menuData,
                                halls = halls,
                                ownWaiter = ownWaiter,
                                waiters = waiters,
                                orderInfo = OrderInfo(
                                    waiter = waiters.find { it.rkId == orderWaiter.rkId }
                                        ?: orderWaiter.toWaiter(),
                                    table = halls.halls.flatMap { it.tables }.find {
                                        it.rkId == orderTable.rkId
                                    } ?: orderTable.toTable()
                                ),
                                //TODO delete
//                                newOrderItems = testNewItems
                            )
                        }
                    } catch (e: Exception) {
                        log.e { "Catch exception in combine flows: $e" }
                        e.printStackTrace()
                        EditOrderScreenState.Error(errorType = ErrorType.from(e))
                    }
                } else {
                    EditOrderScreenState.Loading
                }
            }.collect { newScreenState: EditOrderScreenState ->
                _screenState.value = newScreenState
            }
        }
    }

    fun sendEvent(event: CommonOrderUiEvent) {
        processor.processEvent(event)
    }

    fun sendEvent(event: EditOrderUiEvent) {
        when (event) {
            is EditOrderUiEvent.SelectSavedItem -> selectSavedItem(event.session, event.item)
            is EditOrderUiEvent.UnselectSavedItem -> unselectSavedItem(event.session, event.item)

            is EditOrderUiEvent.SelectAllSavedSessionItems -> selectAllBySession(event.session)
            is UnselectAllSavedSessionItems -> unselectAllBySession(event.session)
            is EditOrderUiEvent.UnselectAllSavedItems -> unselectAllItems()

            is EditOrderUiEvent.OpenVoidSelectorForSelectedItems -> openVoidSelectorForSelectedItems()
            is EditOrderUiEvent.OpenVoidSelectorForSessionItem -> openVoidSelectorForSessionItem(
                event.session,
                event.item
            )

            is EditOrderUiEvent.CloseVoidSelector -> closeVoidSelector()

            is EditOrderUiEvent.RemoveSessionItem -> removeSessionItem(
                event.session,
                event.item,
                event.rkQuantity,
                event.voidRkId
            )

            is RemoveSelectedSavedItem -> removeSelectedSavedItem(event.rkQuantity, event.voidRkId)
            is RemoveAllSelectedSavedItems -> removeAllSelectedSavedItems(event.voidRkId)

            is RefreshData -> refreshData()
            is SaveOrderAndContinue -> addNewItemsToOrder()
            is SaveOrderAndFinish -> addNewItemsToOrder(navigateBack)
        }
    }

    private fun updateOrderInfo(newOrderInfo: OrderInfo): Result<Unit> {
        return withMainState()?.let { state ->
            try {
                if (newOrderInfo == state.orderInfo) return Result.success(Unit)
                if (newOrderInfo.table != null) {
                    coroutineScope.launch {
                        orderService.updateOrderInfo(
                            orderGuid = orderGuid,
                            waiter = newOrderInfo.waiter,
                            table = newOrderInfo.table
                        )
                        transformMainState { it.copy(orderInfo = newOrderInfo) }
                    }
                    return Result.success(Unit)
                }
                Result.failure<Unit>(RuntimeException("Table is null"))
            } catch (e: Exception) {
                Result.failure<Unit>(e)
            }
        } ?: Result.failure<Unit>(RuntimeException("Wrong main state"))
    }

    private fun selectSavedItem(session: Order.Session, item: Order.Dish) {
        log.d { "Start select saved item" }
        if (item.rkQuantity > 0) withMainState()?.apply {
            log.d { "Select saved item - getOrEmptySelectedSavedItems" }
            transformSelectedSavedItems {
                log.d { "Select saved item - transformSelectedSavedItems" }
                add(
                    SelectedSavedDish(
                        rkId = item.rkId,
                        code = item.code,
                        dishUni = item.uni,
                        sessionUni = session.uni
                    )
                )
            }
        }
    }

    private fun unselectSavedItem(session: Order.Session, item: Order.Dish) {
        transformSelectedSavedItems {
            find { it.sessionUni == session.uni && it.dishUni == item.uni }?.let { remove(it) }
        }
    }

    private fun selectAllBySession(target: Order.Session) {
        withMainState().apply {
            target.dishes.filter { savedDish -> savedDish.rkQuantity > 0 }
                .takeIf { it.isNotEmpty() }
                ?.let { list ->
                    transformSelectedSavedItems {
                        addAll(list.map {
                            SelectedSavedDish(
                                rkId = it.rkId,
                                code = it.code,
                                dishUni = it.uni,
                                sessionUni = target.uni
                            )
                        }.toList())
                    }
                }
        }
    }

    private fun unselectAllBySession(target: Order.Session) {
        if (target.dishes.isNotEmpty()) transformSelectedSavedItems {
            removeAll { it.sessionUni == target.uni }
        }
    }

    private fun unselectAllItems() = transformMainState {
        it.copy(selected = Selected.None)
    }

    private fun openVoidSelectorForSelectedItems() {
        transformMainState { it.copy(voidSelectorState = VoidSelectorState.ForSelectedItems) }
    }

    private fun openVoidSelectorForSessionItem(session: Order.Session, item: Order.Dish) {
        transformMainState {
            it.copy(
                voidSelectorState = VoidSelectorState.ForSessionItem(
                    session,
                    item
                )
            )
        }
    }

    private fun closeVoidSelector() {
        transformMainState { it.copy(voidSelectorState = VoidSelectorState.Closed) }
    }

    private fun removeSessionItem(
        session: Order.Session,
        item: Order.Dish,
        rkQuantity: Int,
        voidRkId: String
    ) {
        if (rkQuantity > 0 && rkQuantity <= item.rkQuantity) coroutineScope.launch {
            setRemoving(true)
            orderService.removeItemFromOrderSession(
                orderGuid = orderGuid,
                payload = buildRemoveSession(
                    session = session,
                    item = item,
                    rqQnt = rkQuantity,
                    voidId = voidRkId
                )
            ).onSuccess {
                transformMainState {
                    it.copy(isRemoving = false, voidSelectorState = VoidSelectorState.Closed)
                }
            }
        }
    }

    private fun removeSelectedSavedItem(rkQuantity: Int, voidRkId: String) {
        withMainState()?.apply {
            if (selected is Selected.SavedItems && selected.items.size == 1 && rkQuantity > 0) {
                val target = selected.items.first()
                order.sessions.find { it.uni == target.sessionUni }?.let { session ->
                    session.dishes.find { it.uni == target.dishUni }
                        ?.takeIf { it.rkQuantity >= rkQuantity }?.let { dishInfo ->
                            coroutineScope.launch {
                                setRemoving(true)
                                orderService.removeItemFromOrderSession(
                                    orderGuid = orderGuid,
                                    payload = buildRemoveSession(
                                        session = session,
                                        item = dishInfo,
                                        rqQnt = rkQuantity,
                                        voidId = voidRkId
                                    )
                                ).onSuccess { unselectAllItems() }
                                setRemoving(false)
                            }
                        }
                }

            }
        }
    }

    private fun removeAllSelectedSavedItems(voidRkId: String): Unit {
        withMainState()?.apply {
            if (selected is Selected.SavedItems && selected.items.isNotEmpty()) {
                val payloads = selected.items.groupBy { it.sessionUni }.mapNotNull { entry ->
                    order.sessions.find { it.uni == entry.key }?.let { session ->
                        session.dishes.filter { dish -> entry.value.any { dish.uni == it.dishUni } }
                            .takeIf { it.isNotEmpty() }?.let { items ->
                                buildRemoveSession(session, items, voidRkId)
                            }
                    }
                }
                if (payloads.isNotEmpty()) coroutineScope.launch {
                    setRemoving(true)
                    orderService.removeItemsFromOrderSessions(
                        orderGuid = orderGuid,
                        payload = payloads
                    ).onSuccess { unselectAllItems() }
                    setRemoving(false)
                }
            }
        }
    }

    private fun addNewItemsToOrder(onSuccess: () -> Unit = {}) {
        withMainState()?.let { state ->
            if (state.newOrderItems.isNotEmpty()) coroutineScope.launch {
                setLoading(true)
                orderService.addItemsToOrder(orderGuid = orderGuid, newItems = state.newOrderItems)
                    .onSuccess {
                        onSuccess()
                        transformMainState { it.copy(newOrderItems = listOf()) }
                    }
                setLoading(false)
            }
        }
    }

    private fun refreshData() {
        _screenState.value = EditOrderScreenState.Loading
        coroutineScope.launch {
            orderService.refreshCurrentOrder()
        }
    }

    private fun setLoading(value: Boolean) {
        log.d { "Set isSaving to $value" }
        transformMainState { it.copy(isSaving = value) }
    }

    private fun setRemoving(value: Boolean) {
        log.d { "Set isRemoving to $value" }
        transformMainState { it.copy(isRemoving = value) }
    }

    private fun transformMainState(
        action: (state: EditOrderScreenState.Main) -> EditOrderScreenState
    ) {
        val currentState = _screenState.value
        if (currentState is EditOrderScreenState.Main) {
            _screenState.value = action(currentState)
        }
    }

    private fun withMainState(): EditOrderScreenState.Main? {
        val state = _screenState.value
        return if (state is EditOrderScreenState.Main) state else null
    }

    private fun transformSelectedSavedItems(transform: MutableSet<SelectedSavedDish>.() -> Unit) {
        log.d { "Start transform selected saved items" }
        withMainState()?.getOrEmptySelectedSavedItems()?.let { selectedItems ->
            log.d { "Transform selected saved items - getOrEmptySelectedSavedItems. SelectedItems: $selectedItems" }
            transformMainState {
                log.d { "Transform selected saved items - transformMainState. State: $it" }
                it.copyCommon(selected = Selected.SavedItems(items = selectedItems.mutate(transform)))
            }
        }
    }

    private fun EditOrderScreenState.Main.getOrEmptySelectedSavedItems(): Set<SelectedSavedDish> {
        return when (selected) {
            is Selected.SavedItems -> selected.items
            else -> emptySet()
        }
    }

    private fun Order.Waiter.toWaiter() = Waiter("", rkId, guid, code, name)
    private fun Order.Table.toTable() = TableInfo("", rkId, guid, code, name, "", "", "")
}