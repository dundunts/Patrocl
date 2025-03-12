package org.turter.patrocl.presentation.orders.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetMenu(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    menuOptions: List<MenuOption>
) {
    if (showBottomSheet) {
        Box(modifier = modifier) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = onDismiss
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Опции",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        textAlign = TextAlign.Center
                    )
                    menuOptions.map { MenuOptionItem(menuOption = it) }
                }
            }
        }
    }
}

@Composable
private fun MenuOptionItem(
    modifier: Modifier = Modifier,
    menuOption: MenuOption
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        onClick = menuOption.onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = menuOption.icon, contentDescription = "${menuOption.label} icon")
            Text(text = menuOption.label)
        }
    }
}

data class MenuOption(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)