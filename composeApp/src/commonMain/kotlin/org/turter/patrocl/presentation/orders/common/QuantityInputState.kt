package org.turter.patrocl.presentation.orders.common

import com.benasher44.uuid.Uuid

sealed class QuantityInputState {

    data object Closed : QuantityInputState()

    data class Opened(val uuid: Uuid) : QuantityInputState()

}