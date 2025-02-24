package org.turter.patrocl.data.remote.client

import org.turter.patrocl.data.dto.source.voids.CompanyOrderItemVoidsData

interface OrderItemVoidApiClient {
    suspend fun getOrderItemVoidsForUser(): Result<CompanyOrderItemVoidsData>
}