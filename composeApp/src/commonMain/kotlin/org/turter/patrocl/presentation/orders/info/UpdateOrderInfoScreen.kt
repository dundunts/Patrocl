package org.turter.patrocl.presentation.orders.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parametersOf
import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.presentation.components.FullscreenLoader
import org.turter.patrocl.presentation.components.footer.ConfirmDismissFooter
import org.turter.patrocl.presentation.orders.common.components.TablePickerDialog
import org.turter.patrocl.presentation.orders.common.components.WaiterPickerDialog
import org.turter.patrocl.presentation.orders.common.OrderInfo

class UpdateOrderInfoScreen(
    private val initInfo: OrderInfo,
    private val halls: HallsData,
    private val availableWaiters: List<Waiter>,
    private val onSave: (info: OrderInfo) -> Result<Unit>
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val vm: UpdateOrderInfoViewModel = koinScreenModel {
            parametersOf(
                initInfo,
                halls,
                availableWaiters,
                onSave,
                { navigator.pop() })
        }

        val focusManager = LocalFocusManager.current

        val state by vm.screenState.collectAsState()
        val info = state.info
        val containerPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { vm.sendEvent(UpdateOrderInfoUiEvent.NavigateBack) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back icon"
                            )
                        }
                    },
                    title = { Text("Свойства заказа") }
                )
            }
        ) { innerPadings ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadings),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                //table
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(containerPadding)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Стол:",
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { vm.sendEvent(UpdateOrderInfoUiEvent.OpenTablePicker) }) {
                            Text(text = "Выбрать")
                        }
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = info.table?.name ?: "...",
                        overflow = TextOverflow.Ellipsis
                    )
                }
                //waiter
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(containerPadding)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Официант:",
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { vm.sendEvent(UpdateOrderInfoUiEvent.OpenWaiterPicker) }) {
                            Text(text = "Выбрать")
                        }
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = info.waiter.name,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.weight(1f))
                //footer
                ConfirmDismissFooter(
                    confirmLabel = "Сохранить",
                    dismissLabel = "Отменить",
                    confirmEnabled = info.table != null,
                    onConfirm = { vm.sendEvent(UpdateOrderInfoUiEvent.UpdateInfo) },
                    onDismiss = { vm.sendEvent(UpdateOrderInfoUiEvent.NavigateBack) }
                )
            }
        }

        TablePickerDialog(
            isOpened = state.isTablePickerOpened,
            tables = state.halls.halls.flatMap { it.tables }.sortedBy { it.name },
            selectedTable = info.table,
            onDismiss = { vm.sendEvent(UpdateOrderInfoUiEvent.CloseTablePicker) },
            onSelectTable = { vm.sendEvent(UpdateOrderInfoUiEvent.SetTable(it)) }
        )

        WaiterPickerDialog(
            isOpened = state.isWaiterPickerOpened,
            waiters = state.availableWaiters.sortedBy { it.name },
            selectedWaiter = info.waiter,
            onDismiss = { vm.sendEvent(UpdateOrderInfoUiEvent.CloseWaiterPicker) },
            onSelectWaiter = { vm.sendEvent(UpdateOrderInfoUiEvent.SetWaiter(it)) }
        )

        FullscreenLoader(state.isLoading)
    }
}