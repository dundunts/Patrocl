package org.turter.patrocl.domain.model.order

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4

data class NewOrderItem(
    val uuid: Uuid = uuid4(),
    var dishId: String,
    var dishName: String,
    var rkQuantity: Int,
    var modifiers: List<Modifier>
) {
    data class Modifier(
        val type: Type,
        val modifierId: String,
        val name: String,
        var count: Int,
        val content: String
    ) {
        enum class Type {
            REGULAR,
            COMMENT
        }

        companion object {
            fun regular(modifierId: String, name: String, quantity: Int = 1) = Modifier(
                type = Type.REGULAR,
                modifierId = modifierId,
                name = name,
                count = quantity,
                content = ""
            )

            fun comment(content: String) = Modifier(
                type = Type.COMMENT,
                modifierId = "",
                name = content,
                count = 1,
                content = content
            )
        }
    }
}
