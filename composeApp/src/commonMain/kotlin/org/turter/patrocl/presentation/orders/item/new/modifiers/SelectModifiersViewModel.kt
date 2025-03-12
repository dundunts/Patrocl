package org.turter.patrocl.presentation.orders.item.new.modifiers

import cafe.adriel.voyager.core.model.ScreenModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo
import org.turter.patrocl.domain.model.menu.StationModifierInfo
import org.turter.patrocl.domain.model.order.NewOrderItem

sealed class SelectModifiersUiEvent {
    data class AddModifier(val modInfo: StationModifierInfo) : SelectModifiersUiEvent()
    data class RemoveModifier(val mod: NewOrderItem.Modifier) : SelectModifiersUiEvent()

    data class IncreaseModifierCount(val modInfo: StationModifierInfo) : SelectModifiersUiEvent()
    data class DecreaseModifierCount(val modInfo: StationModifierInfo) : SelectModifiersUiEvent()
    data class SetModifierCount(val modId: String, val count: Int) : SelectModifiersUiEvent()

    data class AddComment(val content: String) :
    SelectModifiersUiEvent()
    data class OpenCommentInput(val modRkId: String) : SelectModifiersUiEvent()
    data object CloseCommentInput : SelectModifiersUiEvent()

    data class OpenSpecialGroup(val schemeDetails: ModifierSchemeInfo.Details) :
        SelectModifiersUiEvent()

    data class OpenGeneralGroup(val groupRkId: String) : SelectModifiersUiEvent()
    data object ToParentGroup : SelectModifiersUiEvent()
    data object ToRoot : SelectModifiersUiEvent()

    data object SaveOrderItem : SelectModifiersUiEvent()
    data object OnClose : SelectModifiersUiEvent()
}

class SelectModifiersViewModel(
    private val originalItem: NewOrderItem,
    private val menuData: MenuTreeData,
    private val autoOpened: Boolean,
    private val onSave: (item: NewOrderItem) -> Unit,
    private val onClose: () -> Unit
) : ScreenModel {
    private val log = Logger.withTag("SelectModifiersViewModel")

    private val _screenState = MutableStateFlow<SelectModifierScreenState>(
        SelectModifierScreenState.Main(
            autoOpened = autoOpened,
            itemModifiers = originalItem.modifiers,
            modifiersRkIdMap = menuData.modifiersRkIdMap,
            groupsRkIdMap = menuData.modifiersGroupRkIdMap,
            modifierScheme = menuData.modifiersSchemeRkIdMap[originalItem.dishInfo.modiScheme],
            rootGeneralGroup = menuData.modifiersGroupRkIdMap[menuData.rootModifierGroupRkId],
            selectedModifierGroupState = SelectedModifierGroupState.Root
        )
    )

    val screenState = _screenState.asStateFlow()

    fun sendEvent(event: SelectModifiersUiEvent) {
        when (event) {
            is SelectModifiersUiEvent.AddModifier -> addModifier(event.modInfo)
            is SelectModifiersUiEvent.RemoveModifier -> removeModifier(event.mod)

            is SelectModifiersUiEvent.IncreaseModifierCount -> increaseModifierCount(event.modInfo)
            is SelectModifiersUiEvent.DecreaseModifierCount -> decreaseModifierCount(event.modInfo)
            is SelectModifiersUiEvent.SetModifierCount -> TODO()

            is SelectModifiersUiEvent.AddComment -> addComment(event.content)
            is SelectModifiersUiEvent.OpenCommentInput -> openCommentInput(event.modRkId)
            is SelectModifiersUiEvent.CloseCommentInput -> closeCommentInput()

            is SelectModifiersUiEvent.OpenSpecialGroup -> openSpecialGroup(event.schemeDetails)
            is SelectModifiersUiEvent.OpenGeneralGroup -> openGeneralGroup(event.groupRkId)
            is SelectModifiersUiEvent.ToParentGroup -> toParentGroup()
            is SelectModifiersUiEvent.ToRoot -> toRoot()
            is SelectModifiersUiEvent.SaveOrderItem -> saveOrderItem()
            is SelectModifiersUiEvent.OnClose -> close()
        }
    }

    private fun addModifier(modInfo: StationModifierInfo) {
        withMainState()?.itemModifiers?.let { modifiers ->
            if (modifiers.none { it.modifierId == modInfo.rkId }) {
                transformNewOrderItems {
                    add(modInfo.toNewOrderItemModifier())
                }
            }
        }
    }

    private fun addComment(content: String) {
        withMainState()?.let { state ->
            if (state.commentInputState is CommentInputState.Opened) {
                val modRkId = state.commentInputState.modRkId
                if (state.itemModifiers.none { it.modifierId == modRkId && it.content == content }) {
                    transformNewOrderItems {
                        add(NewOrderItem.Modifier.comment(
                            modifierId = modRkId,
                            name = content,
                            content = content
                        ))
                    }
                }
            }
        }
    }

    private fun removeModifier(mod: NewOrderItem.Modifier) {
        transformNewOrderItems {
            find { it.getUniqueKey() == mod.getUniqueKey() }?.let { modifier ->
                remove(modifier)
            }
        }
    }

    private fun increaseModifierCount(modInfo: StationModifierInfo) {
        transformNewOrderItems {
            find { it.modifierId == modInfo.rkId }?.let { modifier ->
                if (!modInfo.useLimitedQnt || modifier.count < modInfo.maxOneDish) {
                    this[indexOf(modifier)] = modifier.copy(count = modifier.count + 1)
                }
            } ?: add(modInfo.toNewOrderItemModifier())
        }
    }

    private fun decreaseModifierCount(modInfo: StationModifierInfo) {
        transformNewOrderItems {
            find { it.modifierId == modInfo.rkId }?.let { modifier ->
                if (modifier.count > 1) {
                    this[indexOf(modifier)] = modifier.copy(count = modifier.count - 1)
                }
                else remove(modifier)
            }
        }
    }

    private fun openCommentInput(modRkId: String) {
        transformMainState {
            it.copy(commentInputState = CommentInputState.Opened(modRkId))
        }
    }

    private fun closeCommentInput() {
        transformMainState {
            it.copy(commentInputState = CommentInputState.Closed)
        }
    }

    private fun openSpecialGroup(schemeDetails: ModifierSchemeInfo.Details) {
        menuData.modifiersGroupRkIdMap[schemeDetails.modifiersGroupRkId]?.let { group ->
            transformMainState {
                it.copy(
                    selectedModifierGroupState = SelectedModifierGroupState.Special(
                        groupInfo = group,
                        details = schemeDetails
                    )
                )
            }
        }
    }

    private fun openGeneralGroup(groupRkId: String) {
        menuData.modifiersGroupRkIdMap[groupRkId]?.let { group ->
            transformMainState {
                it.copy(
                    selectedModifierGroupState = if (group.rkId == menuData.rootModifierGroupRkId) {
                        SelectedModifierGroupState.Root
                    } else {
                        SelectedModifierGroupState.Common(group)
                    }
                )
            }
        }
    }


    private fun toParentGroup() {
        withMainState()?.selectedModifierGroupState?.let { selectedGroupState ->
            when (selectedGroupState) {
                is SelectedModifierGroupState.Common -> openGeneralGroup(selectedGroupState.groupInfo.mainParentIdent)

                is SelectedModifierGroupState.Special -> toRoot()

                else -> {}
            }
        }
    }

    private fun toRoot() {
        transformMainState { it.copy(selectedModifierGroupState = SelectedModifierGroupState.Root) }
    }

    private fun saveOrderItem() {
        withMainState()?.itemModifiers?.let {
            onSave(originalItem.copy(modifiers = it))
            onClose()
        }
    }

    private fun close() {
        onClose()
    }

    private fun transformMainState(
        action: (state: SelectModifierScreenState.Main) -> SelectModifierScreenState
    ) {
        val currentState = _screenState.value
        if (currentState is SelectModifierScreenState.Main) {
            _screenState.value = action(currentState)
        }
    }

    private fun withMainState(): SelectModifierScreenState.Main? {
        val state = _screenState.value
        return if (state is SelectModifierScreenState.Main) state else null
    }

    private fun List<NewOrderItem.Modifier>.mutate(mutation: MutableList<NewOrderItem.Modifier>.() -> Unit)
            : List<NewOrderItem.Modifier> {
        return this.toMutableList().apply(mutation).toList()
    }

    private fun transformNewOrderItems(transform: MutableList<NewOrderItem.Modifier>.() -> Unit) {
        transformMainState {
            it.copy(itemModifiers = it.itemModifiers.mutate(transform))
        }
    }

    private fun StationModifierInfo.toNewOrderItemModifier(count: Int = 1) =
        NewOrderItem.Modifier.regular(
            modifierId = rkId,
            name = name,
            count = count
        )

}