package org.turter.patrocl.data.local.repository

import org.turter.patrocl.data.local.LocalSource
import org.turter.patrocl.data.local.entity.menu.CategoryLocal
import org.turter.patrocl.data.local.entity.voids.OrderItemVoidLocal

interface OrderItemVoidLocalRepository: LocalSource<List<OrderItemVoidLocal>> {
}