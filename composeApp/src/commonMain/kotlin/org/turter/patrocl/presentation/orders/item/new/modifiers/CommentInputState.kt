package org.turter.patrocl.presentation.orders.item.new.modifiers

sealed class CommentInputState {

    data object Closed: CommentInputState()

    data class Opened(val modRkId: String): CommentInputState()

}