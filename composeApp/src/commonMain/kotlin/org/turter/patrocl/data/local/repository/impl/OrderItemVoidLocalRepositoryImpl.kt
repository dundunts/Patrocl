package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.voids.OrderItemVoidLocal
import org.turter.patrocl.data.local.handleMultiResult
import org.turter.patrocl.data.local.logFetchingEntityListWith
import org.turter.patrocl.data.local.repository.OrderItemVoidLocalRepository

class OrderItemVoidLocalRepositoryImpl : OrderItemVoidLocalRepository {
    private val log = Logger.withTag("OrderItemVoidLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun count(): Long {
        log.d { "Start counting" }
        return realm.query<OrderItemVoidLocal>()
            .count()
            .find()
    }

    override fun get(): Flow<Result<List<OrderItemVoidLocal>>> = flow {
        log.d { "Start Entity getAll flow" }
        realm.query<OrderItemVoidLocal>()
            .asFlow()
            .collect { res ->
                emit(handleMultiResult(res).logFetchingEntityListWith(log))
            }
    }

    override suspend fun replace(data: List<OrderItemVoidLocal>) {
        cleanUp()
        log.d { "Start writing to realm order item voids, total elements: ${data.size}" }
        data.forEach { element ->
            realm.write {
                copyToRealm(element)
            }
        }
        log.d { "Complete writing to realm order item voids, total elements: ${data.size}" }
    }

    override suspend fun cleanUp() {
        log.d { "Start cleanup Entity" }
        realm.write {
            delete(this.query<OrderItemVoidLocal>())
        }
    }
}