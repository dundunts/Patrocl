package org.turter.patrocl.domain.fetcher

import org.turter.patrocl.domain.model.voids.OrderItemVoidInfo

interface OrderItemVoidFetcher: SourceFetcher<List<OrderItemVoidInfo>> {
}