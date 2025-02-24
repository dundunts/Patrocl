package org.turter.patrocl.data.local.repository

import kotlinx.coroutines.flow.Flow
import org.turter.patrocl.data.dto.enums.SourceDataType
import org.turter.patrocl.data.local.entity.version.CompanySourceDataVersionLocal

interface CompanySourceDataVersionLocalRepository {
    fun getForSource(dataType: SourceDataType): Flow<Result<CompanySourceDataVersionLocal>>
    suspend fun updateVersion(entity: CompanySourceDataVersionLocal)
    suspend fun deleteVersionFor(dataType: SourceDataType)
}