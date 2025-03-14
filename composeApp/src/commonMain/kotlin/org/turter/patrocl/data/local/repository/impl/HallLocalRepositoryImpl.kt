package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.hall.HallLocal
import org.turter.patrocl.data.local.entity.hall.TableLocal
import org.turter.patrocl.data.local.handleMultiResult
import org.turter.patrocl.data.local.logFetchingEntityListWith
import org.turter.patrocl.data.local.repository.HallLocalRepository

class HallLocalRepositoryImpl : HallLocalRepository {
    private val log = Logger.withTag("HallLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun count(): Long {
        log.d { "Start counting" }
        return realm.query<HallLocal>()
            .count()
            .find()
    }

    override fun get(): Flow<Result<List<HallLocal>>> = flow {
        log.d { "Start Entity getAll flow" }
        realm.query<HallLocal>()
            .asFlow()
            .collect { res ->
                emit(handleMultiResult(res).logFetchingEntityListWith(log))
            }
    }

    override suspend fun replace(data: List<HallLocal>) {
        cleanUp()
        log.d { "Start writing to realm halls and tables, total halls: ${data.size}" }
        data.forEach { element ->
            realm.write {
                copyToRealm(element)
            }
        }
        log.d { "Complete writing to realm halls and tables, total halls: ${data.size}" }
    }

    override suspend fun cleanUp() {
        log.d { "Start cleanup Entity" }
        realm.write {
            delete(this.query<HallLocal>())
            delete(this.query<TableLocal>())
        }
    }
}