package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.person.EmployeeLocal
import org.turter.patrocl.data.local.handleSingleResult
import org.turter.patrocl.data.local.logFetchingEntityWith
import org.turter.patrocl.data.local.repository.EmployeeLocalRepository

class EmployeeLocalRepositoryImpl : EmployeeLocalRepository {
    private val log = Logger.withTag("EmployeeLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun get(): Flow<Result<EmployeeLocal>> = flow {
        log.d { "Start Entity getAll flow" }
        realm.query<EmployeeLocal>()
            .first()
            .asFlow()
            .collect { res ->
                emit(handleSingleResult(res).logFetchingEntityWith(log))
            }
    }

    override suspend fun replace(data: EmployeeLocal) {
        realm.write {
            log.d { "Clean up previous Entity" }
            delete(this.query<EmployeeLocal>())
            log.d { "Write to realm Entity: $data" }
            copyToRealm(data)
        }
    }

    override suspend fun cleanUp() {
        log.d { "Start cleanup Entity" }
        realm.write {
            delete(this.query<EmployeeLocal>())
        }
    }
}