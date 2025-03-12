package org.turter.patrocl.presentation.orders.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.presentation.components.input.RkQuantityInputField

@Composable
fun RkQuantityInputDialog(
    item: NewOrderItem,
    onConfirm: (rqQuantity: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var newQuantity by remember { mutableIntStateOf(item.rkQuantity) }

    val isZeroOrBelow = newQuantity <= 0
    val errorText = if (isZeroOrBelow) "Кол-во должно быть больше 0" else null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Количество") },
        icon = { Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit icon") },
        text = {
            RkQuantityInputField(
                initQnt = item.rkQuantity,
                onQuantityChange = { newQuantity = it },
                qntDigits = item.dishInfo.qntDecDigits,
                isError = isZeroOrBelow,
                errorText = errorText
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        confirmButton = {
            TextButton(
                enabled = !isZeroOrBelow,
                onClick = {
                    if (!isZeroOrBelow) {
                        onConfirm(newQuantity)
                        onDismiss()
                    }
                }
            ) {
                Text("Сохранить".uppercase())
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
                newQuantity = 0
            }) {
                Text("Отмена".uppercase())
            }
        },
    )
}