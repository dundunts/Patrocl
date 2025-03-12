package org.turter.patrocl.presentation.orders.create.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import org.turter.patrocl.presentation.orders.common.CommonOrderUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderTopAppBar(
    onBack: () -> Unit,
    tableName: String,
    waiterName: String
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back icon"
                )
            }
        },
        title = {
            Column {
                Text(
                    text = "Новый заказ",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Стол: $tableName. " +
                            "Официант: $waiterName",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}