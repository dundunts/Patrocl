package org.turter.patrocl.presentation.orders.edit.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.turter.patrocl.domain.model.voids.OrderItemVoidInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveSelectedOrderItemsDialog(
    count: Int,
    voids: List<OrderItemVoidInfo>,
    onDismiss: () -> Unit,
    onConfirm: (voidRkId: String) -> Unit
) {
    if (voids.isNotEmpty()) {
        var selectedVoid by remember { mutableStateOf(voids.first()) }
        var dropDownExpanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = onDismiss,
            icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon") },
            title = { Text("Удалить выбранные элементы($count шт.)?") },
            text = {
                ExposedDropdownMenuBox(
                    expanded = dropDownExpanded,
                    onExpandedChange = {
                        dropDownExpanded = !dropDownExpanded
                    }
                ) {
                    TextField(
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
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(selectedVoid.rkId)
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