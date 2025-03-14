package org.turter.patrocl.data.remote.client

import org.turter.patrocl.data.dto.source.dataversion.DataVersionDto

interface DataVersionApiClient {
    suspend fun getDataVersionsForCompany(companyId: String): Result<List<DataVersionDto>>
}