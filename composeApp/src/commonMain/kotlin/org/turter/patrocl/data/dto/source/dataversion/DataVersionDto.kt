package org.turter.patrocl.data.dto.source.dataversion

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.turter.patrocl.domain.model.enums.SourceDataType

@Serializable
data class DataVersionDto(
    val id: String,
    val companyId: String,
    val dataType: SourceDataType,
    val count: Long,
    val time: LocalDateTime
)

