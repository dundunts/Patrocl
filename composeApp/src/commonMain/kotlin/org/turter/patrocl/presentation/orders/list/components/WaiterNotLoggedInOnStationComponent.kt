package org.turter.patrocl.presentation.orders.list.components

import androidx.compose.runtime.Composable
import org.turter.patrocl.presentation.error.ErrorMessageTemplate

@Composable
fun WaiterNotLoggedInOnStationComponent(onRefresh: () -> Unit) {
    ErrorMessageTemplate(
        title = "Официант не зарегестрирован на станции",
        description = "Для взаимодействия с заказами необходимо выполнить вход на соответствующей станции",
        actionTitle = "Обновить",
        onAction = onRefresh
    )
}