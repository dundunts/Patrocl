package org.turter.patrocl.presentation.orders.item.new.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.presentation.components.CircularLoader
import org.turter.patrocl.presentation.orders.item.new.modifiers.components.SelectModifierMainComponent

class SelectModifiersScreen(
    private val item: NewOrderItem,
    private val menuData: MenuTreeData,
    private val autoOpened: Boolean = false,
    private val onSave: (item: NewOrderItem) -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val vm = koinScreenModel<SelectModifiersViewModel> {
            parametersOf(
                item,
                menuData,
                autoOpened,
                onSave,
                { navigator.pop() })
        }

        when(val state = vm.screenState.collectAsState().value) {
            is SelectModifierScreenState.Main -> SelectModifierMainComponent(vm = vm, state = state)

            else -> { CircularLoader() }
        }
    }
}

