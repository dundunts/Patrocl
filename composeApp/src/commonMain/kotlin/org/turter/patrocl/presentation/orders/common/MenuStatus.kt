package org.turter.patrocl.presentation.orders.common

import org.turter.patrocl.domain.model.menu.CategoryInfo

data class MenuState(
    val currentStatus: MenuStatus,
    val targetStatus: MenuStatus,
    val currentCategory: CategoryInfo?
)

sealed class MenuStatus {

    data object Opened: MenuStatus()

    data object Closed: MenuStatus()

}