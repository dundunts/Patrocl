package org.turter.patrocl.domain.model.menu

import org.turter.patrocl.domain.model.stoplist.StopListItem
import org.turter.patrocl.domain.model.voids.OrderItemVoidInfo

data class MenuTreeData(
    val rootCategoryRkId: String,
    val dishRkIdMap: Map<String, StationDishInfo>,
    val categoryRkIdMap: Map<String, CategoryInfo>,

    val rootModifierGroupRkId: String,
    val modifiersRkIdMap: Map<String, StationModifierInfo>,
    val modifiersGroupRkIdMap: Map<String, ModifierGroupInfo>,
    val modifiersSchemeRkIdMap: Map<String, ModifierSchemeInfo>,

    val orderItemVoids: List<OrderItemVoidInfo>,

    val stopListDishRkIdMap: Map<String, StopListItem>


) {
    override fun toString(): String {
        return "MenuTreeData(" +
                "rootCategoryRkId='$rootCategoryRkId', " +
                "dishRkIdMap.size=${dishRkIdMap.size}, " +
                "categoryRkIdMap.size=${categoryRkIdMap.size}, " +
                "rootModifierGroupRkId='$rootModifierGroupRkId', " +
                "modifiersRkIdMap.size=${modifiersRkIdMap.size}, " +
                "modifiersGroupRkIdMap.size=${modifiersGroupRkIdMap.size}, " +
                "modifiersSchemeRkIdMap.size=${modifiersSchemeRkIdMap.size}, " +
                "orderItemVoids=${orderItemVoids.size}, " +
                "stopListDishRkIdMap.size=${stopListDishRkIdMap.size}" +
                ")"
    }
}
