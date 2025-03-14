package org.turter.patrocl.data.local.repository

import kotlinx.coroutines.flow.Flow
import org.turter.patrocl.domain.model.enums.SourceDataType
import org.turter.patrocl.data.local.entity.version.CompanySourceDataVersionLocal

interface CompanySourceDataVersionLocalRepository {
    fun getAllForCompany(companyId: String): Result<List<CompanySourceDataVersionLocal>>
    fun getAllForCompanyFlow(companyId: String): Flow<Result<List<CompanySourceDataVersionLocal>>>
    fun getForSource(dataType: SourceDataType, companyId: String): Result<CompanySourceDataVersionLocal>
    fun getForSourceFlow(dataType: SourceDataType, companyId: String): Flow<Result<CompanySourceDataVersionLocal>>
    suspend fun updateVersion(entity: CompanySourceDataVersionLocal)
    suspend fun deleteVersionFor(dataType: SourceDataType)
}