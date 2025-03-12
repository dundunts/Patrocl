package org.turter.patrocl.presentation.orders.item.new.edit

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.turter.patrocl.domain.model.menu.deprecated.MenuData
import org.turter.patrocl.domain.model.order.NewOrderItem

class EditNewOrderItemScreen(
    private val item: NewOrderItem,
    private val menuData: MenuData,
    private val onSave: (item: NewOrderItem) -> Unit,
    private val onDelete: (item: NewOrderItem) -> Unit
) : Screen {
    @Composable
    override fun Content() {
//        val navigator = LocalNavigator.currentOrThrow
//
//        val vm: EditNewOrderItemViewModel = koinScreenModel {
//            parametersOf(item, menuData, onSave, onDelete)
//        }
//
//        val state by vm.screenState.collectAsState()
//
//        var isDishPickerOpened by remember { mutableStateOf(false) }
//        var isModifierPickerOpened by remember { mutableStateOf(false) }
//        var isCommentCreatorOpened by remember { mutableStateOf(false) }
//        var isConfirmDeleteDialogOpened by remember { mutableStateOf(false) }
//
//        Scaffold(
//            topBar = {
//                EditNewOrderItemTopAppBar(
//                    onBack = { navigator.pop() },
//                    onDelete = { isConfirmDeleteDialogOpened = true }
//                )
//            },
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = {
//                        vm.sendEvent(EditNewOrderItemUiEvent.SaveChangesAndThen { navigator.pop() })
//                    }
//                ) {
//                    Icon(imageVector = Icons.Default.Check, contentDescription = "Accept icon")
//                }
//            }
//        ) { innerPadding ->
//            Column(
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .padding(vertical = 8.dp)
//                    .fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp)
//                    ) {
//                        Text(
//                            modifier = Modifier
//                                .padding(16.dp),
//                            text = "Основная информация",
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.Bottom
//                        ) {
//                            OutlinedButton(
//                                modifier = Modifier
//                                    .height(OutlinedTextFieldDefaults.MinHeight)
//                                    .weight(1f),
//                                shape = RoundedCornerShape(4.dp),
//                                colors = ButtonDefaults.outlinedButtonColors().copy(
//                                    contentColor = MaterialTheme.colorScheme.onSurface
//                                ),
//                                onClick = { isDishPickerOpened = true }
//                            ) {
//                                Text(
//                                    text = state.dish.name,
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
//                                    overflow = TextOverflow.Ellipsis
//                                )
//                            }
//                            Spacer(Modifier.width(8.dp))
//                            FloatNaturalInput(
//                                modifier = Modifier.fillMaxWidth(0.25f),
//                                initialValue = state.originalItem.rkQuantity,
//                                onValueChange = {
//                                    vm.sendEvent(
//                                        EditNewOrderItemUiEvent.SetQuantity(
//                                            it
//                                        )
//                                    )
//                                }
//                            )
//                        }
//                    }
//                }
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(horizontal = 16.dp, vertical = 8.dp)
//                    ) {
//                        Text(
//                            modifier = Modifier
//                                .padding(16.dp),
//                            text = "Модификаторы:",
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//
//                        Row(
//                            verticalAlignment = Alignment.Bottom,
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            OutlinedButton(
//                                modifier = Modifier
//                                    .height(OutlinedTextFieldDefaults.MinHeight)
//                                    .weight(2f),
//                                shape = RoundedCornerShape(4.dp),
//                                colors = ButtonDefaults.outlinedButtonColors().copy(
//                                    contentColor = MaterialTheme.colorScheme.onSurface
//                                ),
//                                onClick = { isModifierPickerOpened = true }
//                            ) {
//                                Text(
//                                    text = state.targetModifier?.name ?: "Выбрать",
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
//                                    overflow = TextOverflow.Ellipsis
//                                )
//                            }
//
//                            IntNaturalInput(
//                                modifier = Modifier.weight(1f),
//                                value = state.targetModifierQuantity,
//                                label = "Кол-во"
//                            ) {
//                                vm.sendEvent(EditNewOrderItemUiEvent.SetTargetModifierQuantity(it))
//                            }
//
//                            OutlinedIconButton(
//                                modifier = Modifier
//                                    .height(OutlinedTextFieldDefaults.MinHeight)
//                                    .weight(1f),
//                                shape = RoundedCornerShape(4.dp),
//                                enabled = state.targetModifier != null && state.targetModifierQuantity > 0,
//                                onClick = { vm.sendEvent(EditNewOrderItemUiEvent.AddTargetModifier) }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Check,
//                                    contentDescription = "Add"
//                                )
//                            }
//                        }
//
//                        Spacer(Modifier.height(8.dp))
//
//                        OutlinedButton(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(OutlinedTextFieldDefaults.MinHeight),
//                            shape = RoundedCornerShape(4.dp),
//                            colors = ButtonDefaults.outlinedButtonColors()
//                                .copy(contentColor = MaterialTheme.colorScheme.onSurface),
//                            onClick = { isCommentCreatorOpened = true }
//                        ) {
//                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit icon")
//                            Spacer(Modifier.width(4.dp))
//                            Text(
//                                text = "Написать комментарий",
//                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
//                            )
//                        }
//
//                        Spacer(Modifier.height(8.dp))
//
//                        LazyColumn(
//                            modifier = Modifier.weight(1f),
//                            verticalArrangement = Arrangement.spacedBy(4.dp)
//                        ) {
//                            if (state.modifiers.isEmpty()) {
//                                item {
//                                    Column(
//                                        modifier = Modifier
//                                            .height(50.dp)
//                                            .animateItem(),
//                                    ) {
//                                        HorizontalDivider()
//                                        Spacer(Modifier.weight(1f))
//                                        Text(
//                                            modifier = Modifier.fillMaxWidth(),
//                                            text = "Список пуст",
//                                            textAlign = TextAlign.Center
//                                        )
//                                        Spacer(Modifier.weight(1f))
//                                        HorizontalDivider()
//                                    }
//                                }
//                            }
//                            items(
//                                items = state.modifiers,
//                                key = { it.modifierId + it.content }
//                            ) { modifierItem ->
//                                Column(modifier = Modifier.animateItem()) {
//                                    HorizontalDivider()
//                                    Row(
//                                        modifier = Modifier.fillMaxWidth(),
//                                        horizontalArrangement = Arrangement.SpaceBetween,
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
//                                        Text("${modifierItem.name} x${modifierItem.count}")
//                                        IconButton(onClick = {
//                                            vm.sendEvent(
//                                                EditNewOrderItemUiEvent.RemoveModifier(
//                                                    modifierItem
//                                                )
//                                            )
//                                        }) {
//                                            Icon(
//                                                Icons.Default.Delete,
//                                                contentDescription = "Remove"
//                                            )
//                                        }
//                                    }
//                                    HorizontalDivider()
//                                }
//                            }
//                            item {
//                                Spacer(modifier = Modifier.height(88.dp))
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        state.interceptedAdding?.let { intercepted ->
//            InterceptedAddingDialog(
//                warningType = intercepted.warningType,
//                onDismiss = { vm.sendEvent(EditNewOrderItemUiEvent.RejectInterceptedAdding) },
//                onConfirm = {
//                    vm.sendEvent(
//                        EditNewOrderItemUiEvent.ConfirmInterceptedAddingAndThen { navigator.pop() }
//                    )
//                }
//            )
//        }
//
//        if (isModifierPickerOpened) ModifierPickerDialog(
//            rootGroup = menuData.rootModifiersGroup,
//            allModifiers = menuData.modifiers,
//            onDismiss = { isModifierPickerOpened = false },
//            onConfirm = { vm.sendEvent(EditNewOrderItemUiEvent.SelectTargetModifier(it)) }
//        )
//
//        if (isDishPickerOpened) DishPickerDialog(
//            dishes = menuData.dishes,
//            currentDish = state.dish,
//            onDismiss = { isDishPickerOpened = false },
//            onConfirm = { vm.sendEvent(EditNewOrderItemUiEvent.SelectDish(it)) }
//        )
//
//        if (isCommentCreatorOpened) {
//            CreateCommentDialog(
//                modifiers = state.modifiers,
//                onConfirm = { vm.sendEvent(EditNewOrderItemUiEvent.AddComment(it)) },
//                onDismiss = { isCommentCreatorOpened = false }
//            )
//        }
//
//        if (isConfirmDeleteDialogOpened) RemoveItemDialog(
//            expanded = isConfirmDeleteDialogOpened,
//            title = "Удалить данный элемент?",
//            onDismiss = { isConfirmDeleteDialogOpened = false },
//            onConfirm = {
//                vm.sendEvent(EditNewOrderItemUiEvent.DeleteItemAndThen { navigator.pop() })
//            }
//        )
    }
}