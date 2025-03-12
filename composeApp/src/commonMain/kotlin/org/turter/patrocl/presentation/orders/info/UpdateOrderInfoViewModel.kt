package org.turter.patrocl.presentation.orders.info

import cafe.adriel.voyager.core.model.ScreenModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.hall.TableInfo
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.presentation.orders.common.OrderInfo

sealed class UpdateOrderInfoUiEvent {
    data object NavigateBack : UpdateOrderInfoUiEvent()

    data object OpenTablePicker : UpdateOrderInfoUiEvent()
    data object CloseTablePicker : UpdateOrderInfoUiEvent()
    data class SetTable(val table: TableInfo) : UpdateOrderInfoUiEvent()

    data object OpenWaiterPicker : UpdateOrderInfoUiEvent()
    data object CloseWaiterPicker : UpdateOrderInfoUiEvent()
    data class SetWaiter(val waiter: Waiter) : UpdateOrderInfoUiEvent()

    data object UpdateInfo : UpdateOrderInfoUiEvent()
}

class UpdateOrderInfoViewModel(
    private val initInfo: OrderInfo,
    private val halls: HallsData,
    private val availableWaiters: List<Waiter>,
    private val onSave: (info: OrderInfo) -> Result<Unit>,
    private val navigateBack: () -> Unit
) : ScreenModel {
    private val log = Logger.withTag("UpdateOrderInfoViewModel")

    private val _screenState = MutableStateFlow(
        UpdateOrderInfoScreenState(
            info = initInfo,
            halls = halls,
            availableWaiters = availableWaiters,
            isTablePickerOpened = initInfo.table == null
        )
    )

    val screenState = _screenState.asStateFlow()

    fun sendEvent(event: UpdateOrderInfoUiEvent) {
        when(event) {
            is UpdateOrderInfoUiEvent.NavigateBack -> navigateBack()

            is UpdateOrderInfoUiEvent.OpenTablePicker -> openTablePicker()
            is UpdateOrderInfoUiEvent.CloseTablePicker -> closeTablePicker()
            is UpdateOrderInfoUiEvent.SetTable -> setTable(event.table)

            is UpdateOrderInfoUiEvent.OpenWaiterPicker -> openWaiterPicker()
            is UpdateOrderInfoUiEvent.CloseWaiterPicker -> closeWaiterPicker()
            is UpdateOrderInfoUiEvent.SetWaiter -> setWaiter(event.waiter)

            is UpdateOrderInfoUiEvent.UpdateInfo -> updateInfo()
        }
    }

    private fun openTablePicker() {
        transformState { it.copy(isTablePickerOpened = true) }
    }

    private fun closeTablePicker() {
        transformState { it.copy(isTablePickerOpened = false) }
    }

    private fun setTable(table: TableInfo) {
        transformState { it.copy(info = it.info.copy(table = table)) }
    }

    private fun openWaiterPicker() {
        transformState { it.copy(isWaiterPickerOpened = true) }
    }

    private fun closeWaiterPicker() {
        transformState { it.copy(isWaiterPickerOpened = false) }
    }

    private fun setWaiter(waiter: Waiter) {
        transformState { it.copy(info = it.info.copy(waiter = waiter)) }
    }

    private fun updateInfo() {
        withState().info.apply {
            if (table != null) {
                transformState { it.copy(isLoading = true) }
                onSave(this).also { result ->
                    transformState { it.copy(isLoading = false) }
                    result.onSuccess { navigateBack() }
                }
            }
        }
    }

    private fun transformState(
        transform: (state: UpdateOrderInfoScreenState) -> UpdateOrderInfoScreenState
    ) {
        _screenState.value = transform(_screenState.value)
    }

    private fun withState(): UpdateOrderInfoScreenState = _screenState.value

}