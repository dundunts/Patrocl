package org.turter.patrocl.presentation.orders.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.turter.patrocl.domain.model.menu.CategoryInfo
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.stoplist.StopListItem
import org.turter.patrocl.presentation.components.input.SearchTextField
import org.turter.patrocl.presentation.orders.common.DishWarningType
import org.turter.patrocl.presentation.orders.common.MenuState
import org.turter.patrocl.presentation.orders.common.MenuStatus

@Composable
fun RestaurantMenuSelector(
    modifier: Modifier = Modifier,
    categoriesMap: Map<String, CategoryInfo>,
    dishesMap: Map<String, StationDishInfo>,
    stopListMap: Map<String, StopListItem>,
    rootCategoryRkId: String,
    initCategoryRkId: String? = null,
    searchFieldInteractionSource: MutableInteractionSource,
//    menuOpened: Boolean,
    menuState: MenuState,
    setCurrentCategory: (category: CategoryInfo) -> Unit,
    switchMenuOpened: () -> Unit,
    addNewItem: (dishInfo: StationDishInfo) -> Unit
) {
//    val rootCategory = categoriesMap[rootCategoryRkId]
//    val initCategory = initCategoryRkId?.let { categoriesMap[it] }

    var dishSearch by remember { mutableStateOf("") }

    val searchedDishes = dishesMap.values.filter { it.name.contains(dishSearch, true) }
        .sortedBy { it.name }.toList()

//    var currentCategory by remember { mutableStateOf(initCategory ?: rootCategory) }
    val menuOpened = menuState.targetStatus is MenuStatus.Opened
    val currentCategory = menuState.currentCategory
    val currentCategoryTitle = currentCategory?.name ?: ""
    val isRootCategory = currentCategory?.rkId?.let { it == rootCategoryRkId } ?: false
    val childCategories = currentCategory?.childIds?.mapNotNull { categoriesMap[it] }
        ?.sortedBy { it.name } ?: listOf()
    val childDishes = currentCategory?.dishIds?.mapNotNull { dishesMap[it] }
        ?.sortedBy { it.name }
        ?: listOf()


    val setCategory: (categoryRkId: String) -> Unit = { id ->
        categoriesMap[id]?.let { setCurrentCategory(it) }
    }

    val toParentCategory: () -> Unit = {
        currentCategory?.mainParentIdent?.let { categoriesMap[it] }?.let { setCurrentCategory(it) }
    }

    val toRootCategory: () -> Unit = {
        setCategory(rootCategoryRkId)
    }

    val isToParentCategoryAvailable = currentCategory?.mainParentIdent
        ?.let(categoriesMap::containsKey) ?: false

    val getDishWarningType: (dishRkId: String) -> DishWarningType = { dishRkId ->
        stopListMap[dishRkId]?.let { DishWarningType.of(it.onStop, it.remainingCount) }
            ?: DishWarningType.None
    }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.height(70.dp)) {
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchTextField(
                        modifier = Modifier.weight(1f),
                        value = dishSearch,
                        interactionSource = searchFieldInteractionSource,
                        onValueChange = { dishSearch = it },
                        onClearClick = { dishSearch = "" }
                    )
                    Spacer(Modifier.width(12.dp))
                    IconButton(onClick = { switchMenuOpened() }) {
                        if (menuOpened) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Down icon"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Up icon"
                            )
                        }
                    }
                }
            }

            HorizontalDivider(Modifier.padding(horizontal = 12.dp))

            if (dishSearch.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 4.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isRootCategory) {
                        Text(
                            modifier = Modifier.padding(start = 40.dp),
                            text = "Меню"
                        )
                    } else {
                        IconButton(
                            enabled = isToParentCategoryAvailable,
                            onClick = toParentCategory
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back icon"
                            )
                        }
                        Text(
                            modifier = Modifier.weight(1f),
                            text = currentCategoryTitle
                        )
                        Spacer(Modifier.width(12.dp))
                        IconButton(onClick = toRootCategory) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home icon"
                            )
                        }
                    }
                }

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .weight(1f),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(items = childCategories, key = { it.rkId }) { category ->
                        CategoryItem(
                            title = category.name,
                            onClick = { setCategory(category.rkId) }
                        )
                    }
                    items(items = childDishes, key = { it.rkId }) { dish ->
                        DishItem(
                            title = dish.name,
                            warningType = getDishWarningType(dish.rkId),
                            onClick = { addNewItem(dish) }
                        )
                    }
                    item(key = "menuSpacer") { Spacer(Modifier.height(200.dp)) }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 12.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(items = searchedDishes, key = { it.rkId }) { dish ->
                        SearchedDishItem(
                            modifier = Modifier.fillMaxWidth(),
                            name = dish.name,
                            path = dish.categPath,
                            warningType = getDishWarningType(dish.rkId),
                            onClick = { addNewItem(dish) }
                        )
                    }
                    item(key = "searchSpacer") { Spacer(Modifier.height(200.dp)) }
                }
            }
        }
    }
}