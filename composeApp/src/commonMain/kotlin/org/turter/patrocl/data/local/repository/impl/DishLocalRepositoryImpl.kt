package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.menu.DishLocal
import org.turter.patrocl.data.local.handleMultiResult
import org.turter.patrocl.data.local.logFetchingEntityListWith
import org.turter.patrocl.data.local.repository.DishLocalRepository

class DishLocalRepositoryImpl : DishLocalRepository {
    private val log = Logger.withTag("DishLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun get(): Flow<Result<List<DishLocal>>> = flow {
        log.d { "Start Entity getAll flow" }
        realm.query<DishLocal>()
            .asFlow()
            .collect { res ->
                emit(handleMultiResult(res).logFetchingEntityListWith(log))
            }
    }

    override suspend fun replace(data: List<DishLocal>) {
        cleanUp()
        log.d { "Start writing to realm dishes, total elements: ${data.size}" }
        data.forEach { element ->
            realm.write {
                copyToRealm(element)
            }
        }
        log.d { "Complete writing to realm dishes, total elements: ${data.size}" }
    }

    override suspend fun cleanUp() {
        log.d { "Start cleanup Entity" }
        realm.write {
            delete(this.query<DishLocal>())
        }
    }
}