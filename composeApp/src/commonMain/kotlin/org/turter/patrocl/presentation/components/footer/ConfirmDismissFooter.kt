package org.turter.patrocl.presentation.components.footer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmDismissFooter(
    modifier: Modifier = Modifier,
    confirmLabel: String,
    dismissLabel: String,
    confirmEnabled: Boolean = true,
    closeEnabled: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                enabled = closeEnabled,
                shape = RectangleShape,
                onClick = onDismiss
            ) {
                Text(dismissLabel)
            }
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                enabled = confirmEnabled,
                shape = RectangleShape,
                onClick = onConfirm
            ) {
                Text(confirmLabel)
            }
        }
    }
}