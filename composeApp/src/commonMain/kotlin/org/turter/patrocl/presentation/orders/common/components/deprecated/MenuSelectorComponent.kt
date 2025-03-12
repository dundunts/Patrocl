package org.turter.patrocl.presentation.orders.common.components.deprecated

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.turter.patrocl.domain.model.menu.CategoryInfo
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.stoplist.StopListItem
import org.turter.patrocl.presentation.orders.common.DishWarningType

@Composable
fun MenuTreeDishSelector(
    modifier: Modifier = Modifier,
    rootCategoryId: String,
    initCategoryId: String? = null,
    categoriesMap: Map<String, CategoryInfo>,
    dishesMap: Map<String, StationDishInfo>,
    stopListMap: Map<String, StopListItem>,
    onDishClick: (StationDishInfo) -> Unit
) {
    var currentCategory by remember {
        mutableStateOf(
            categoriesMap[initCategoryId ?: rootCategoryId]
        )
    }
    var childCategories by remember {
        mutableStateOf(currentCategory?.childIds?.mapNotNull { categoriesMap[it] }
            ?.sortedBy { it.name } ?: listOf())
    }
    var childDishes by remember {
        mutableStateOf(currentCategory?.dishIds?.mapNotNull { dishesMap[it] }
            ?.sortedBy { it.name }
            ?: listOf())
    }

    val setCategory: (categoryRkId: String) -> Unit = { id ->
        categoriesMap[id]?.let { currentCategory = it }
    }

    val toParentCategory: () -> Unit = {
        currentCategory?.mainParentIdent?.let { categoriesMap[it] }?.let { currentCategory = it }
    }

    val isToParentCategoryAvailable = currentCategory?.mainParentIdent
        ?.let(categoriesMap::containsKey) ?: false

    val getDishWarningType: (dishRkId: String) -> DishWarningType = { dishRkId ->
        stopListMap[dishRkId]?.let { DishWarningType.of(it.onStop, it.remainingCount) }
            ?: DishWarningType.None
    }

    Surface(
        modifier = modifier
            .fillMaxHeight(0.5f)
            .padding(horizontal = 8.dp)
    ) {
        Column {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isToParentCategoryAvailable) item {
                    Button(
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onClick = toParentCategory
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
                items(
                    items = childCategories,
                    key = { it.guid }
                ) { category ->
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { setCategory(category.rkId) },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(text = category.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
                items(
                    items = childDishes,
                    key = { it.guid }
                ) { dish ->
                    DishComponent(
                        modifier = Modifier.fillMaxWidth(),
                        name = dish.name,
                        type = getDishWarningType(dish.id),
                        onClick = { onDishClick(dish) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DishComponent(
    modifier: Modifier,
    name: String,
    type: DishWarningType,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            when (type) {
                is DishWarningType.OnStop -> Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Lock icon"
                )

                is DishWarningType.LowRemain -> Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "Warning icon"
                )

                is DishWarningType.None -> {}
            }
            Text(text = name, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

