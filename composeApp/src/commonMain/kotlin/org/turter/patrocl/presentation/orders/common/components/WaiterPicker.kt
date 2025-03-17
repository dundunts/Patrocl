package org.turter.patrocl.presentation.orders.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.presentation.components.input.SearchTextField
import org.turter.patrocl.presentation.components.dialog.FullscreenDialog
import org.turter.patrocl.ui.icons.Person_apron

@Composable
fun WaiterPickerDialog(
    isOpened: Boolean,
    waiters: List<Waiter>,
    selectedWaiter: Waiter,
    onDismiss: () -> Unit,
    onSelectWaiter: (Waiter) -> Unit
) {
    var searchString by remember { mutableStateOf("") }
    var newSelectedWaiter by remember { mutableStateOf(selectedWaiter) }
    val filteredWaiters = waiters
        .filter { if (searchString.isNotEmpty()) it.name.contains(searchString) else true }
        .toList()

    if (isOpened) {
        FullscreenDialog(
            icon = { Icon(imageVector = Person_apron, contentDescription = "Waiter icon") },
            label = "Выбор официанта",
            content = {
                val focusManager = LocalFocusManager.current

                Column(modifier = Modifier.fillMaxSize()) {
                    SearchTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        value = searchString,
                        onValueChange = { searchString = it },
                        placeholder = { Text(text = "Имя...") },
                        textColor = MaterialTheme.colorScheme.onSurface,
                        colors = OutlinedTextFieldDefaults.colors()
                    )

                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 4.dp, vertical = 4.dp),
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items = filteredWaiters, key = { it.guid }) { waiter ->
                            WaiterPickerElement(
                                modifier = Modifier.height(80.dp),
                                isSelect = waiter.guid == newSelectedWaiter?.guid,
                                waiterName = waiter.name,
                                onClick = {
                                    newSelectedWaiter = waiter
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                }
            },
            dismissLabel = "Назад",
            onDismiss = onDismiss,
            confirmLabel = "Выбрать",
            onConfirm = {
                onSelectWaiter(newSelectedWaiter)
                onDismiss()
            }
        )
    }
}

@Composable
private fun WaiterPickerElement(
    modifier: Modifier = Modifier,
    isSelect: Boolean,
    waiterName: String,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelect) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(durationMillis = 300)
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelect) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300)
    )

    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(text = waiterName, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}