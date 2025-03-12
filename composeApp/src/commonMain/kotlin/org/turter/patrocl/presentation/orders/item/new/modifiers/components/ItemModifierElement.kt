package org.turter.patrocl.presentation.orders.item.new.modifiers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.turter.patrocl.domain.model.order.NewOrderItem

@Composable
fun ItemModifierElement(
    modifier: Modifier = Modifier,
    itemModifier: NewOrderItem.Modifier,
    onDelete: () -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainerLow

    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier.fillMaxWidth()
            .background(containerColor)
    ) {
        HorizontalDivider(thickness = 1.dp, color = contentColor)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .defaultMinSize(minHeight = 80.dp)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(2f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = itemModifier.count.toString(),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = contentColor
                )
            }
            VerticalDivider(thickness = 1.dp, color = contentColor)
            Box(
                modifier = Modifier.weight(7f)
                    .fillMaxHeight()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart).padding(horizontal = 4.dp),
                    text = itemModifier.name,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = contentColor
                )
            }
            VerticalDivider(thickness = 1.dp, color = contentColor)
            Box(
                modifier = Modifier.weight(3f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = contentColor)
    }
}