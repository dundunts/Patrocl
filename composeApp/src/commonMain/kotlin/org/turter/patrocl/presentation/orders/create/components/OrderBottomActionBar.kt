package org.turter.patrocl.presentation.orders.create.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.turter.patrocl.presentation.orders.common.Selected
import org.turter.patrocl.ui.icons.Restaurant_menu

@Composable
fun OrderBottomActionBar(
    selectedState: Selected,
    onOrderInfo: () -> Unit,
    onRestaurantMenu: () -> Unit,
    onSave: () -> Unit,
    onFinish: () -> Unit,
    onDefaultMenu: () -> Unit,
    onUnselect: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onSelectedItemMenu: () -> Unit
) {
    BottomAppBar(
//        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        actions = {
            AnimatedContent(
                targetState = selectedState,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut() using SizeTransform(clip = false)
                }
            ) { targetState ->
                when (targetState) {

                    is Selected.NewItem -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = onUnselect) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Localized description"
                                )
                            }
                            IconButton(onClick = onRestaurantMenu) {
                                Icon(
                                    imageVector = Restaurant_menu,
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = onMoveUp) {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = onMoveDown) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = onSelectedItemMenu) {
                                Icon(
                                    Icons.Filled.Menu,
                                    contentDescription = "Localized description",
                                )
                            }
                        }
                    }
                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = onOrderInfo) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Localized description"
                                )
                            }
                            IconButton(onClick = onRestaurantMenu) {
                                Icon(
                                    imageVector = Restaurant_menu,
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = onSave) {
                                Icon(
                                    Icons.Filled.Create,
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = onFinish) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = onDefaultMenu) {
                                Icon(
                                    Icons.Filled.Menu,
                                    contentDescription = "Localized description",
                                )
                            }
                        }
                    }
                }

            }
        }
    )
}