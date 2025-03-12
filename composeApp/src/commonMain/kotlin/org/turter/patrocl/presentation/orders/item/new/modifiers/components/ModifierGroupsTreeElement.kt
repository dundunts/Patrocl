package org.turter.patrocl.presentation.orders.item.new.modifiers.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.turter.patrocl.domain.model.menu.StationModifierInfo
import org.turter.patrocl.ui.icons.Remove

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModifierGroupElement(
    modifier: Modifier = Modifier,
    title: String,
    status: ModifierGroupPickStatus = ModifierGroupPickStatus.None,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    val badgeColor = when (status) {
        is ModifierGroupPickStatus.None -> null
        else -> if (status.isOk) Color.Green else Color.Red
    }
    Box(
        modifier = modifier.height(100.dp)
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
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 4.dp),
                    text = title,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Start
                )
            }
        }
        badgeColor?.let { color ->
            Badge(modifier = Modifier.align(Alignment.TopEnd), containerColor = color)
        }
    }
}

@Composable
fun ModifierItem(
    modifier: Modifier = Modifier,
    modInfo: StationModifierInfo,
    count: Int,
    incCount: () -> Unit,
    decCount: () -> Unit,
    enable: Boolean = true
) {
    val containerColor by animateColorAsState(
        targetValue = if (enable) CardDefaults.cardColors().containerColor
        else CardDefaults.cardColors().disabledContainerColor,
        animationSpec = tween(durationMillis = 500)
    )

    val contentColor by animateColorAsState(
        targetValue = if (enable) CardDefaults.cardColors().contentColor
        else CardDefaults.cardColors().disabledContentColor,
        animationSpec = tween(durationMillis = 500)
    )

    val incEnabled = !modInfo.useLimitedQnt || count < modInfo.maxOneDish
    val decEnabled = count > 0

    Box(
        modifier = modifier.height(100.dp)
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .weight(2f),
                        text = modInfo.name,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(enabled = enable && decEnabled, onClick = decCount) {
                            Icon(imageVector = Remove, contentDescription = "Remove icon")
                        }
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = count.toString()
                        )
                        IconButton(enabled = enable && incEnabled, onClick = incCount) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add icon")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModifierInputNameItem(
    modifier: Modifier = Modifier,
    modInfo: StationModifierInfo,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (enable) CardDefaults.cardColors().containerColor
        else CardDefaults.cardColors().disabledContainerColor,
        animationSpec = tween(durationMillis = 500)
    )

    val contentColor by animateColorAsState(
        targetValue = if (enable) CardDefaults.cardColors().contentColor
        else CardDefaults.cardColors().disabledContentColor,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = modifier.height(100.dp)
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = enable, onClick = onClick),
            colors = CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = modInfo.name,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}