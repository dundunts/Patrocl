package org.turter.patrocl.domain.model.order

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.utils.toRealQuantity
import org.turter.patrocl.utils.toRealSum

data class NewOrderItem(
    val uuid: Uuid = uuid4(),
//    var dishId: String,
//    var dishName: String,
    val dishInfo: StationDishInfo,
    var rkQuantity: Int,
//    val rkPrice: Int = 100000,
    val modifiers: List<Modifier>
) {
    fun getQuantity(): Float = rkQuantity.toRealQuantity()

    fun getAmount(): Float = dishInfo.price.toRealSum() * getQuantity()

    data class Modifier(
        val type: Type,
        val modifierId: String,
        val name: String,
        val count: Int,
        val content: String
    ) {
        enum class Type {
            REGULAR,
            COMMENT
        }

        companion object {
            fun regular(modifierId: String, name: String, count: Int = 1) = Modifier(
                type = Type.REGULAR,
                modifierId = modifierId,
                name = name,
                count = count,
                content = ""
            )

            fun customComment(content: String) = Modifier(
                type = Type.COMMENT,
                modifierId = "",
                name = content,
                count = 1,
                content = content
            )

            fun comment(modifierId: String, name: String, count: Int = 1, content: String) = Modifier(
                type = Type.COMMENT,
                modifierId = modifierId,
                name = name,
                count = count,
                content = content
            )
        }

        fun getUniqueKey(): String = "${this.modifierId}:${this.content}"
    }
}
