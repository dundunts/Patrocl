package org.turter.patrocl.presentation.orders.edit

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
import org.turter.patrocl.presentation.orders.edit.EditOrderUiEvent.RefreshData
import org.turter.patrocl.presentation.orders.edit.components.EditOrderComponent
import org.turter.patrocl.presentation.orders.info.UpdateOrderInfoScreen
import org.turter.patrocl.presentation.orders.item.new.modifiers.SelectModifiersScreen

class EditOrderScreen(private val orderGuid: String): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val vm: EditOrderViewModel = koinScreenModel {
            parametersOf(
                orderGuid,
                { item: NewOrderItem, menuData: MenuTreeData, autoOpened: Boolean,
                  onSave: (item: NewOrderItem) -> Unit ->
                    navigator.push(SelectModifiersScreen(item, menuData, autoOpened, onSave))
                },
                { info: OrderInfo, halls: HallsData, availableWaiters: List<Waiter>,
                  onSave: (info: OrderInfo) -> Result<Unit> ->
                    navigator.push(UpdateOrderInfoScreen(info, halls, availableWaiters, onSave))
                },
                { navigator.pop() }
            )
        }

        when (val currentScreenState = vm.screenState.collectAsState().value) {
            is EditOrderScreenState.Main -> {
                EditOrderComponent(
                    vm = vm,
                    state = currentScreenState
                )
            }

            is EditOrderScreenState.Error -> {
                ErrorComponent(
                    error = currentScreenState.errorType,
                    onRetry = { vm.sendEvent(RefreshData) }
                )
            }

            else -> { CircularLoader() }
        }
    }
}