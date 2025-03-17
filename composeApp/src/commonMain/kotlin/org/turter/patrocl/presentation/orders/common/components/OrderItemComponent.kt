package org.turter.patrocl.presentation.orders.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.presentation.components.SwipeToDismissComponent
import org.turter.patrocl.utils.toFormatTime
import org.turter.patrocl.utils.toFormattedString
import org.turter.patrocl.utils.toRealQuantity
import org.turter.patrocl.utils.toRealSum

@Composable
fun SwipeToDismissWrapper(
    modifier: Modifier = Modifier,
    onStartToEnd: () -> Boolean,
    onEndToStart: () -> Boolean,
    content: @Composable () -> Unit
) {
    SwipeToDismissComponent(
        modifier = modifier,
        onStartToEnd = onStartToEnd,
        onEndToStart = onEndToStart,
        bg = { dismissState ->
            val direction = dismissState.dismissDirection

            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                    SwipeToDismissBoxValue.StartToEnd -> Color.Green
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                }
            )

            val alignment = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                SwipeToDismissBoxValue.Settled -> Alignment.Center
            }
            val icon = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Create
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                SwipeToDismissBoxValue.Settled -> Icons.Default.Delete
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Localized description",
                    modifier = Modifier.scale(scale)
                )
            }
        }
    ) {
        content()
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewOrderItemCard(
    modifier: Modifier = Modifier,
    item: NewOrderItem,
    enabled: Boolean = true,
    select: Boolean = false,
    onCountClick: () -> Unit = {},
    onCountLongClick: () -> Unit = {},
    onTitleClick: () -> Unit = {},
    onTitleLongClick: () -> Unit = {},
    onTitleDoubleClick: () -> Unit = {},
    onAmountClick: () -> Unit = {},
    onAmountLongClick: () -> Unit = {},
    onAmountDoubleClick: () -> Unit = {}
) {
    val containerColor by animateColorAsState(
        targetValue = if (select) MaterialTheme.colorScheme.surfaceContainerHigh
        else MaterialTheme.colorScheme.surfaceContainerLow,
        animationSpec = tween(durationMillis = 300)
    )

    val contentColor by animateColorAsState(
        targetValue = if (select) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300)
    )

    val badgesContainerColor = MaterialTheme.colorScheme.secondaryContainer

    val badgesContentColor = MaterialTheme.colorScheme.onSecondaryContainer

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
                    .fillMaxHeight()
                    .combinedClickable(
                        enabled = enabled,
                        onClick = onCountClick,
                        onLongClick = onCountLongClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.getQuantity().toString(),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = contentColor
                )
            }
            VerticalDivider(thickness = 1.dp, color = contentColor)
            Box(
                modifier = Modifier.weight(7f)
                    .fillMaxHeight()
                    .combinedClickable(
                        enabled = enabled,
                        onClick = onTitleClick,
                        onLongClick = onTitleLongClick,
                        onDoubleClick = onTitleDoubleClick
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = item.dishInfo.name,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = contentColor
                    )
                    if (item.modifiers.isNotEmpty()) {
                        Column(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            item.modifiers.forEach {
                                Badge(
                                    containerColor = badgesContainerColor,
                                    contentColor = badgesContentColor
                                ) {
                                    Text(
                                        text = "${it.name} x ${it.count}",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
            VerticalDivider(thickness = 1.dp, color = contentColor)
            Box(
                modifier = Modifier.weight(3f)
                    .fillMaxHeight()
                    .combinedClickable(
                        onClick = onAmountClick,
                        onLongClick = onAmountLongClick,
                        onDoubleClick = onAmountDoubleClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.getAmount().toFormattedString(2),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = contentColor
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = contentColor)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedOrderItemCard(
    modifier: Modifier = Modifier,
    item: Order.Dish,
    enabled: Boolean = true,
    select: Boolean = false,
    onCountClick: () -> Unit = {},
    onCountLongClick: () -> Unit = {},
    onTitleClick: () -> Unit = {},
    onTitleLongClick: () -> Unit = {},
    onTitleDoubleClick: () -> Unit = {},
    onAmountClick: () -> Unit = {},
    onAmountLongClick: () -> Unit = {},
    onAmountDoubleClick: () -> Unit = {}
) {
    val containerColor by animateColorAsState(
        targetValue = if (select) MaterialTheme.colorScheme.surfaceContainerHigh
        else MaterialTheme.colorScheme.surfaceContainerLow,
        animationSpec = tween(durationMillis = 300)
    )

    val contentColor by animateColorAsState(
        targetValue = if (select) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300)
    )

    val badgesContainerColor = MaterialTheme.colorScheme.secondaryContainer

    val badgesContentColor = MaterialTheme.colorScheme.onSecondaryContainer

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
                    .fillMaxHeight()
                    .combinedClickable(
                        enabled = enabled,
                        onClick = onCountClick,
                        onLongClick = onCountLongClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.rkQuantity.toRealQuantity().toString(),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = contentColor
                )
            }
            VerticalDivider(thickness = 1.dp, color = contentColor)
            Box(
                modifier = Modifier.weight(7f)
                    .fillMaxHeight()
                    .combinedClickable(
                        enabled = enabled,
                        onClick = onTitleClick,
                        onLongClick = onTitleLongClick,
                        onDoubleClick = onTitleDoubleClick
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = item.name,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = contentColor
                    )
                    if (item.modifiers.isNotEmpty()) {
                        Column(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            item.modifiers.forEach {
                                Badge(
                                    containerColor = badgesContainerColor,
                                    contentColor = badgesContentColor
                                ) {
                                    Text(
                                        text = "${it.name} x ${it.count}",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
            VerticalDivider(thickness = 1.dp, color = contentColor)
            Box(
                modifier = Modifier.weight(3f)
                    .fillMaxHeight()
                    .combinedClickable(
                        onClick = onAmountClick,
                        onLongClick = onAmountLongClick,
                        onDoubleClick = onAmountDoubleClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.rkAmount.toRealSum().toFormattedString(2),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = contentColor
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = contentColor)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SessionCard(
    modifier: Modifier = Modifier,
    session: Order.Session,
    enabled: Boolean = true,
    select: Boolean = false,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val containerColor by animateColorAsState(
        targetValue = if (select) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surfaceContainerLow,
        animationSpec = tween(durationMillis = 300)
    )

    val contentColor by animateColorAsState(
        targetValue = if (select) MaterialTheme.colorScheme.onSecondaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300)
    )

    Column(
        modifier = modifier.fillMaxWidth()
            .background(containerColor)
            .combinedClickable(
                enabled = enabled,
                onLongClick = onLongClick,
                onClick = onClick
            )
    ) {
        HorizontalDivider(thickness = 1.dp, color = contentColor)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .defaultMinSize(minHeight = 80.dp)
                .padding(vertical = 4.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Официант: ${session.creator.name}",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = session.formattedTime(),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(thickness = 1.dp, color = contentColor)
    }
}

@Composable
fun OrderItemsGroupsDivider(
    modifier: Modifier = Modifier,
    title: String
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
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title.uppercase(),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.ExtraBold
            )
        }
        HorizontalDivider(thickness = 1.dp, color = contentColor)
    }
}

private fun Order.Session.formattedTime() = startService.toFormatTime()