package org.turter.patrocl.data.remote.client

import org.turter.patrocl.data.dto.source.hall.CompanyHallsData

interface HallApiClient {
    suspend fun getHallsForUser(): Result<CompanyHallsData>

}