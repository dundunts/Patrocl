package org.turter.patrocl.data.local.repository

import kotlinx.coroutines.flow.Flow
import org.turter.patrocl.data.local.entity.company.CompanySourcesInfoLocal

interface CompanySourcesInfoLocalRepository {
    fun get(): Flow<Result<CompanySourcesInfoLocal>>
    suspend fun setRootCategoryRkId(companyId: String, value: String)
    suspend fun setRootModifiersGroupRkId(companyId: String, value: String)
    suspend fun setDefaultHallRkId(companyId: String, value: String)
}