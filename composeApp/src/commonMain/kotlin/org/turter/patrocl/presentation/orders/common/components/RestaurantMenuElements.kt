package org.turter.patrocl.presentation.orders.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.turter.patrocl.presentation.orders.common.DishWarningType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuItemContainer(
    modifier: Modifier = Modifier,
    title: String,
    leadingContent: @Composable (() -> Unit)? = null,
    badgeColor: Color? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.height(80.dp)
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                leadingContent?.invoke()
                Column(modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp)) {
                    Text(
                        text = title,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        badgeColor?.let { color ->
            Badge(modifier = Modifier.align(Alignment.TopEnd), containerColor = color)
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    MenuItemContainer(
        modifier = modifier.fillMaxWidth(),
        title = title,
        leadingContent = {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(
                        Color.Gray,
                        RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
        },
        onClick = onClick
    )
}

@Composable
fun DishItem(
    modifier: Modifier = Modifier,
    title: String,
    warningType: DishWarningType,
    onClick: () -> Unit
) {
    val badgeColor = when (warningType) {
        is DishWarningType.OnStop -> Color.Red

        is DishWarningType.LowRemain -> Color.Yellow

        else -> null
    }
    MenuItemContainer(
        modifier = modifier.fillMaxWidth(),
        title = title,
        leadingContent = {
            Spacer(Modifier.width(16.dp))
        },
        badgeColor = badgeColor,
        onClick = onClick
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchedDishItem(
    modifier: Modifier = Modifier,
    name: String,
    path: String,
    warningType: DishWarningType,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val badgeColor = when (warningType) {
        is DishWarningType.OnStop -> Color.Red

        is DishWarningType.LowRemain -> Color.Yellow

        else -> null
    }
    Box {
        Card(
            modifier = modifier.combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = name,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = path,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textAlign = TextAlign.Start
                )
            }
        }
        badgeColor?.let { color ->
            Badge(modifier = Modifier.align(Alignment.TopEnd), containerColor = color)
        }
    }
}