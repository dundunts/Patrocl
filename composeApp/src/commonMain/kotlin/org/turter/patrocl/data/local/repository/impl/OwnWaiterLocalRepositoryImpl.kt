package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.data.local.repository.OwnWaiterLocalRepository
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.person.WaiterLocal
import org.turter.patrocl.data.local.handleSingleResult
import org.turter.patrocl.data.local.logFetchingEntityWith

class OwnWaiterLocalRepositoryImpl: OwnWaiterLocalRepository {
    private val log = Logger.withTag("OwnWaiterLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun get(): Flow<Result<WaiterLocal>> = flow {
        log.d { "Start Entity getAll flow" }
        realm.query<WaiterLocal>()
            .first()
            .asFlow()
            .collect { res ->
                emit(handleSingleResult(res).logFetchingEntityWith(log))
            }
    }

    override suspend fun replace(data: WaiterLocal) {
        cleanUp()
        realm.write {
            log.d { "Write to realm Entity: $data" }
            copyToRealm(data)
        }
    }

    override suspend fun cleanUp() {
        log.d { "Start cleanup Entity" }
        realm.write {
            delete(this.query<WaiterLocal>())
        }
    }

}