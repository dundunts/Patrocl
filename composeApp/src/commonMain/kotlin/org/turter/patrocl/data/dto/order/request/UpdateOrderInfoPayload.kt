package org.turter.patrocl.data.dto.order.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderInfoPayload(
    val orderGuid: String,
    val waiterCode: String,
    val tableCode: String
)
