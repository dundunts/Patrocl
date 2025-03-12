package org.turter.patrocl.presentation.orders.item.new.modifiers.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.turter.patrocl.domain.model.menu.StationModifierInfo
import org.turter.patrocl.presentation.components.footer.ConfirmDismissFooter
import org.turter.patrocl.presentation.orders.common.components.CreateCommentDialog
import org.turter.patrocl.presentation.orders.item.new.modifiers.CommentInputState
import org.turter.patrocl.presentation.orders.item.new.modifiers.SelectModifierScreenState
import org.turter.patrocl.presentation.orders.item.new.modifiers.SelectModifiersUiEvent
import org.turter.patrocl.presentation.orders.item.new.modifiers.SelectModifiersViewModel
import org.turter.patrocl.presentation.orders.item.new.modifiers.SelectedModifierGroupState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectModifierMainComponent(
    vm: SelectModifiersViewModel,
    state: SelectModifierScreenState.Main
) {
    val selectedState = state.selectedModifierGroupState
    val commentInputState = state.commentInputState
    val itemModifiers = state.itemModifiers
    val modifierScheme = state.modifierScheme

    val getCurrentCount: (modRkId: String) -> Int = { modRkId ->
        state.itemModifiers.find { it.modifierId == modRkId }?.count ?: 0
    }

    val getAvailableModis: (rkIds: List<String>) -> List<StationModifierInfo> = { rkIds ->
        rkIds.mapNotNull { state.modifiersRkIdMap[it] }.sortedBy { it.name }
    }

    val modisListState = rememberLazyListState(
        initialFirstVisibleItemIndex = itemModifiers.size
    )

    var prevItemsSize by remember { mutableIntStateOf(state.itemModifiers.size) }

    LaunchedEffect(itemModifiers) {
        val currentSize = itemModifiers.size
        if (prevItemsSize < currentSize) {
            modisListState.animateScrollToItem(modisListState.layoutInfo.totalItemsCount)
        }
        prevItemsSize = currentSize
    }

    val closeAvailable: Boolean = !state.autoOpened || modifierScheme?.let { scheme ->
        scheme.details.mapNotNull { details ->
            state.groupsRkIdMap[details.modifiersGroupRkId]
                ?.let { group -> ModifierGroupPickStatus.of(itemModifiers, details, group).isOk }
        }.reduce { acc, b -> acc && b }
    } ?: true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Модификаторы") },
                navigationIcon = {
                    IconButton(
                        enabled = closeAvailable,
                        onClick = { vm.sendEvent(SelectModifiersUiEvent.OnClose) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back icon"
                        )
                    }
                }
            )
        }
    ) { innerPaddings ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPaddings)) {
            //modifier list
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                state = modisListState
            ) {
                items(items = state.itemModifiers, key = { it.getUniqueKey() }) { itemModifier ->
                    ItemModifierElement(
                        modifier = Modifier.animateItem(),
                        itemModifier = itemModifier,
                        onDelete = { vm.sendEvent(SelectModifiersUiEvent.RemoveModifier(itemModifier)) }
                    )
                }
            }
            //modifiers selector
            HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth()
                    .weight(3f)
                    .padding(horizontal = 4.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                when (selectedState) {
                    //root
                    is SelectedModifierGroupState.Root -> {
                        modifierScheme?.let { scheme ->
                            item(key = "spec", span = { GridItemSpan(2) }) {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .height(70.dp)
                                        .padding(horizontal = 44.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Специальные",
                                    )
                                }
                            }
                            items(
                                items = scheme.details.sortedBy { it.name },
                                key = { it.id }
                            ) { details ->
                                state.groupsRkIdMap[details.modifiersGroupRkId]?.let { group ->
                                    ModifierGroupElement(
                                        title = details.name,
                                        status = ModifierGroupPickStatus.of(
                                            currentItems = state.itemModifiers,
                                            details = details,
                                            group = group
                                        ),
                                        onClick = {
                                            vm.sendEvent(
                                                SelectModifiersUiEvent.OpenSpecialGroup(
                                                    details
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        state.rootGeneralGroup?.let { rootGroup ->
                            item(key = "common", span = { GridItemSpan(2) }) {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .height(70.dp)
                                        .padding(horizontal = 44.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Общие",
                                    )
                                }
                            }
                            items(
                                items = rootGroup.childIds.mapNotNull { state.groupsRkIdMap[it] }
                                    .filter { it.commonModifier }.sortedBy { it.name },
                                key = { it.id }
                            ) { group ->
                                ModifierGroupElement(
                                    title = group.name,
                                    onClick = {
                                        vm.sendEvent(
                                            SelectModifiersUiEvent.OpenGeneralGroup(
                                                group.rkId
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }

                    //special
                    is SelectedModifierGroupState.Special -> {
                        val modis = getAvailableModis(selectedState.groupInfo.modifierIds)

                        val currentStatus = ModifierGroupPickStatus.of(
                            itemModifiers,
                            selectedState.details,
                            selectedState.groupInfo
                        )
                        val constraintText = when (currentStatus) {
                            is ModifierGroupPickStatus.UpLimit ->
                                "До ${currentStatus.upLimit} включительно. " +
                                        "Текущее кол-во: ${currentStatus.current}"

                            is ModifierGroupPickStatus.DownLimit ->
                                "От ${currentStatus.downLimit} включительно. " +
                                        "Текущее кол-во: ${currentStatus.current}"

                            is ModifierGroupPickStatus.Range -> {
                                if (currentStatus.downLimit == currentStatus.upLimit) {
                                    "Необходимо ${currentStatus.upLimit}. " +
                                            "Текущее кол-во: ${currentStatus.current}"
                                } else {
                                    "От ${currentStatus.downLimit} " +
                                            "до ${currentStatus.upLimit} включительно. " +
                                            "Текущее кол-во: ${currentStatus.current}"
                                }
                            }

                            is ModifierGroupPickStatus.None -> "Без ограничений"
                        }
                        item(key = "specHeader", span = { GridItemSpan(2) }) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .height(70.dp)
                                    .padding(horizontal = 4.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { vm.sendEvent(SelectModifiersUiEvent.ToRoot) }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = "Back icon"
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = selectedState.groupInfo.name
                                    )
                                    Text(
                                        text = constraintText,
                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                    )
                                }
                            }
                        }
                        items(items = modis, key = { it.id }) { modInfo ->
                            val count = getCurrentCount(modInfo.rkId)
                            ModifierItem(
                                modInfo = modInfo,
                                count = count,
                                enable = currentStatus.pickAvailable || count > 0,
                                incCount = {
                                    vm.sendEvent(
                                        SelectModifiersUiEvent.IncreaseModifierCount(
                                            modInfo
                                        )
                                    )
                                },
                                decCount = {
                                    vm.sendEvent(
                                        SelectModifiersUiEvent.DecreaseModifierCount(
                                            modInfo
                                        )
                                    )
                                }
                            )
                        }
                    }

                    //common
                    is SelectedModifierGroupState.Common -> {
                        val modis = getAvailableModis(selectedState.groupInfo.modifierIds)

                        item(key = "commonHeader", span = { GridItemSpan(2) }) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .height(70.dp)
                                    .padding(horizontal = 4.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { vm.sendEvent(SelectModifiersUiEvent.ToParentGroup) }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = "Back icon"
                                    )
                                }
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = selectedState.groupInfo.name
                                )
                                Spacer(Modifier.width(12.dp))
                                IconButton(onClick = { vm.sendEvent(SelectModifiersUiEvent.ToRoot) }) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "Home icon"
                                    )
                                }
                            }
                        }
                        items(items = modis, key = { it.id }) { modInfo ->
                            if (modInfo.inputName) ModifierInputNameItem(
                                modInfo = modInfo,
                                onClick = {
                                    vm.sendEvent(
                                        SelectModifiersUiEvent.OpenCommentInput(modInfo.rkId)
                                    )
                                }
                            )
                            else ModifierItem(
                                modInfo = modInfo,
                                count = getCurrentCount(modInfo.rkId),
                                incCount = {
                                    vm.sendEvent(
                                        SelectModifiersUiEvent.IncreaseModifierCount(modInfo)
                                    )
                                },
                                decCount = {
                                    vm.sendEvent(
                                        SelectModifiersUiEvent.DecreaseModifierCount(modInfo)
                                    )
                                }
                            )
                        }
                    }
                }
            }
            //footer-actions
            ConfirmDismissFooter(
                confirmLabel = "Сохранить",
                dismissLabel = "Отменить",
                confirmEnabled = closeAvailable,
                closeEnabled = closeAvailable,
                onConfirm = { vm.sendEvent(SelectModifiersUiEvent.SaveOrderItem) },
                onDismiss = { vm.sendEvent(SelectModifiersUiEvent.OnClose) }
            )
        }
    }

    if (commentInputState is CommentInputState.Opened) {
        CreateCommentDialog(
            modifiers = itemModifiers,
            onConfirm = { vm.sendEvent(SelectModifiersUiEvent.AddComment(it)) },
            onDismiss = { vm.sendEvent(SelectModifiersUiEvent.CloseCommentInput) }
        )
    }
}
