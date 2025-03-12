package org.turter.patrocl.presentation.stoplist.create

import kotlinx.datetime.LocalDateTime
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.stoplist.StopListItem
import org.turter.patrocl.presentation.error.ErrorType

sealed class CreateStopListItemScreenState {
    data object Initial: CreateStopListItemScreenState()

    data object Loading: CreateStopListItemScreenState()

    data class Main(
        val items: List<StopListItem>,
        val dishes: List<StationDishInfo>,
        val selectedDishRkId: String = "",
        val remainCount: Int = 0,
        val until: LocalDateTime? = null,
        val isCreating: Boolean = false
    ) : CreateStopListItemScreenState() {
        fun isDishNotInStopList(dishRkId: String) = items.none { it.dishRkId == dishRkId }
        fun getSelectedDish() = dishes.find { it.id == selectedDishRkId }
    }

    data class Error(
        val errorType: ErrorType
    ) : CreateStopListItemScreenState()
}