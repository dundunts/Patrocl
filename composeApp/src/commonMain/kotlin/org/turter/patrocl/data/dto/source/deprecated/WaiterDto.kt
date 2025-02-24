package org.turter.patrocl.data.dto.source.deprecated

import kotlinx.serialization.Serializable

@Serializable
data class WaiterDto(
    val employeeId: String,
    val rkId: String,
    val code: String,
    val name: String
) {
}