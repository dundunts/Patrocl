package org.turter.patrocl.presentation.orders.common

import com.benasher44.uuid.Uuid

//TODO clean up
sealed class Selected {
    data object None : Selected()
    data class NewItem(val newItemUuid: Uuid) : Selected()
    data class SavedItems(
        val items: Set<SelectedSavedDish>
    ) : Selected() {
        //TODO remove?
//        fun withSingleItem() = items.getIfSingleItem()
    }
}

//data class UniInfo(
//    val sessionUni: String,
//    val dishUni: String
//)

///

data class SelectedSavedSession(
    val uni: String,
    val lineGuid: String,
    val sessionId: String,
    val uniDishMap: Map<String, SelectedSavedDish>
)

data class SelectedSavedDish(
    val rkId: String,
    val code: String,
    val dishUni: String,
    val sessionUni: String
)