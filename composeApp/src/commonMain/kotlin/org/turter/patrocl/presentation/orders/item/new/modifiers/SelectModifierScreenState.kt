package org.turter.patrocl.presentation.orders.item.new.modifiers

import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.menu.ModifierGroupInfo
import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo
import org.turter.patrocl.domain.model.menu.StationModifierInfo
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.presentation.error.ErrorType
import org.turter.patrocl.presentation.orders.create.CreateOrderScreenState

sealed class SelectModifierScreenState {

    data object Initial : SelectModifierScreenState()

    data object Loading : SelectModifierScreenState()

    data class Main(
        val autoOpened: Boolean,
        val itemModifiers: List<NewOrderItem.Modifier>,
        val modifiersRkIdMap: Map<String, StationModifierInfo>,
        val groupsRkIdMap: Map<String, ModifierGroupInfo>,
        val modifierScheme: ModifierSchemeInfo?,
        val rootGeneralGroup: ModifierGroupInfo?,
//        val generalGroups: List<ModifierGroupInfo>,
        val selectedModifierGroupState: SelectedModifierGroupState,
        val commentInputState: CommentInputState = CommentInputState.Closed
    ) : SelectModifierScreenState()

//    data class Error(val errorType: ErrorType) : SelectModifierScreenState()

}