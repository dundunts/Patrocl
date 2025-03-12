package org.turter.patrocl.presentation.orders.item.new.modifiers.components

import org.turter.patrocl.domain.model.menu.ModifierGroupInfo
import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo
import org.turter.patrocl.domain.model.order.NewOrderItem

sealed class ModifierGroupPickStatus(val isOk: Boolean, val pickAvailable: Boolean) {
    data object None : ModifierGroupPickStatus(true, true)
    data class DownLimit(val downLimit: Int, val current: Int) :
        ModifierGroupPickStatus(current >= downLimit, true)

    data class UpLimit(val upLimit: Int, val current: Int) :
        ModifierGroupPickStatus(current <= upLimit, current < upLimit)

    data class Range(
        val upLimit: Int,
        val downLimit: Int,
        val current: Int
    ) : ModifierGroupPickStatus(current in downLimit..upLimit, current < upLimit)

    //    data object Required : ModifierGroupPickStatus()
//    data object Complete : ModifierGroupPickStatus()

    companion object {
        fun of(
            currentItems: List<NewOrderItem.Modifier>,
            details: ModifierSchemeInfo.Details,
            group: ModifierGroupInfo
        ): ModifierGroupPickStatus {
            val count = currentItems.count { group.modifierIds.contains(it.modifierId) }
            val useUpLimit = details.useUpLimit
            val upLimit = details.upLimit
            val useDownLimit = details.useDownLimit
            val downLimit = details.downLimit

            return if (!useUpLimit && !useDownLimit) None
            else {
//                val satisfied =
                if (useUpLimit) {
                    if (useDownLimit) {
//                        if (count in downLimit..upLimit) Complete else
                        Range(upLimit, downLimit, count)
//                    } else if (count <= upLimit) Complete else UpLimit(upLimit, count)
                    } else UpLimit(upLimit, count)
//                } else if (count >= downLimit) Complete else
                } else DownLimit(downLimit, count)

//                if (satisfied) Complete else Required
            }
        }
    }
}