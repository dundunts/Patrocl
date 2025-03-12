package org.turter.patrocl.presentation.orders.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf
import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.presentation.components.CircularLoader
import org.turter.patrocl.presentation.error.ErrorComponent
import org.turter.patrocl.presentation.orders.common.OrderInfo
import org.turter.patrocl.presentation.orders.create.components.CreateOrderComponent
import org.turter.patrocl.presentation.orders.edit.EditOrderScreen
import org.turter.patrocl.presentation.orders.info.UpdateOrderInfoScreen
import org.turter.patrocl.presentation.orders.item.new.modifiers.SelectModifiersScreen

class CreateOrderScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val vm: CreateOrderViewModel = koinScreenModel {
            parametersOf(
                { item: NewOrderItem, menuData: MenuTreeData, autoOpened: Boolean,
                  onSave: (item: NewOrderItem) -> Unit ->
                    navigator.push(SelectModifiersScreen(item, menuData, autoOpened, onSave))
                },
                { info: OrderInfo, halls: HallsData, availableWaiters: List<Waiter>,
                  onSave: (info: OrderInfo) -> Result<Unit> ->
                    navigator.push(UpdateOrderInfoScreen(info, halls, availableWaiters, onSave))
                },
                { orderGuid: String -> navigator.replace(EditOrderScreen(orderGuid)) },
                { navigator.pop() }
            )
        }

        when (val currentScreenState = vm.screenState.collectAsState().value) {
            is CreateOrderScreenState.Main -> {
                CreateOrderComponent(
                    vm = vm,
                    state = currentScreenState
                )
            }

            is CreateOrderScreenState.Error -> {
                ErrorComponent(
                    error = currentScreenState.errorType,
                    onRetry = { vm.sendEvent(CreateOrderUiEvent.RefreshData) }
                )
            }

            else -> {
                CircularLoader()
            }
        }
    }
}