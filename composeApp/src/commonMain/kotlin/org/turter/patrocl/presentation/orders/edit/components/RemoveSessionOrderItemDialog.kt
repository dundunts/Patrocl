package org.turter.patrocl.presentation.orders.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.turter.patrocl.domain.model.voids.OrderItemVoidInfo
import org.turter.patrocl.presentation.components.input.RkQuantityInputField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveSessionOrderItemDialog(
    itemName: String,
    initRkQnt: Int = 1000,
    maxQuantity: Int,
    qntDigits: Int = 1,
    voids: List<OrderItemVoidInfo>,
    onDismiss: () -> Unit,
    onConfirm: (rkQnt: Int, voidRkId: String) -> Unit
) {
    if (voids.isNotEmpty()) {
        var quantity by remember { mutableIntStateOf(initRkQnt) }
        var selectedVoid by remember { mutableStateOf(voids.first()) }
        var dropDownExpanded by remember { mutableStateOf(false) }

        val isBelowZero = quantity <= 0
        val isGreaterThenMax = quantity > maxQuantity
        val isError = isBelowZero || isGreaterThenMax

        val errorText = if (isBelowZero) "Кол-во должно быть больше 0"
        else if (isGreaterThenMax) "Превышено кол-во блюда" else null

        AlertDialog(
            onDismissRequest = onDismiss,
            icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon") },
            title = { Text("Удалить позицию \"$itemName\"?") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    //void
                    ExposedDropdownMenuBox(
                        expanded = dropDownExpanded,
                        onExpandedChange = {
                            dropDownExpanded = !dropDownExpanded
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedVoid.name,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded) },
                            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                        )

                        ExposedDropdownMenu(
                            expanded = dropDownExpanded,
                            onDismissRequest = { dropDownExpanded = false }
                        ) {
                            voids.forEach { voidInfo ->
                                DropdownMenuItem(
                                    text = { Text(text = voidInfo.name) },
                                    onClick = {
                                        selectedVoid = voidInfo
                                        dropDownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    //qnt
                    RkQuantityInputField(
                        initQnt = initRkQnt,
                        onQuantityChange = { quantity = it },
                        qntDigits = qntDigits,
                        isError = isError,
                        errorText = errorText
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isError,
                    onClick = {
                        onConfirm(quantity, selectedVoid.rkId)
                        onDismiss()
                    }
                ) {
                    Text("Подтвердить".uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Закрыть".uppercase())
                }
            }
        )
    }
}