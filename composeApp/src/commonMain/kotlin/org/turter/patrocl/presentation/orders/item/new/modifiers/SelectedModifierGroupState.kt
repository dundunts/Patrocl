package org.turter.patrocl.presentation.orders.item.new.modifiers

import org.turter.patrocl.domain.model.menu.ModifierGroupInfo
import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo

sealed class SelectedModifierGroupState {

    data object Root : SelectedModifierGroupState()

    data class Common(
        val groupInfo: ModifierGroupInfo
    ) : SelectedModifierGroupState()

    data class Special(
        val groupInfo: ModifierGroupInfo,
        val details: ModifierSchemeInfo.Details,
//        val upLimit: Int,
//        val downLimit: Int,
//        val freeCount: Boolean
    ) : SelectedModifierGroupState()

}