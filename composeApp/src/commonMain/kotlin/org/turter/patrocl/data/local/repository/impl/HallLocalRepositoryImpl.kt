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
import org.turter.patrocl.domain.exception.EmptyLocalDataException

class HallLocalRepositoryImpl : HallLocalRepository {
    private val log = Logger.withTag("HallLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

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
        data.forEach { element ->
            realm.write {
                log.d { "Write to realm Entity: $data" }
                copyToRealm(element)
            }
        }
    }

    override suspend fun cleanUp() {
        log.d { "Start cleanup Entity" }
        realm.write {
            delete(this.query<HallLocal>())
        }
    }
}