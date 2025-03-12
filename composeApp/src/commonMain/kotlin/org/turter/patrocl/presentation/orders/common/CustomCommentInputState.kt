package org.turter.patrocl.presentation.orders.common

import com.benasher44.uuid.Uuid

sealed class CustomCommentInputState {

    data object Closed: CustomCommentInputState()

    data class Opened(val uuid: Uuid): CustomCommentInputState()

}