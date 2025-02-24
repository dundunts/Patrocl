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
)
