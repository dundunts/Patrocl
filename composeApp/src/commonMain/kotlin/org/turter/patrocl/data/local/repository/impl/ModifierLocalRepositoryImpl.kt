package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.menu.ModifierLocal
import org.turter.patrocl.data.local.handleMultiResult
import org.turter.patrocl.data.local.logFetchingEntityListWith
import org.turter.patrocl.data.local.repository.ModifierLocalRepository

class ModifierLocalRepositoryImpl : ModifierLocalRepository {
    private val log = Logger.withTag("DishModifierLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun get(): Flow<Result<List<ModifierLocal>>> = flow {
        log.d { "Start Entity getAll flow" }
        realm.query<ModifierLocal>()
            .asFlow()
            .collect { res ->
                emit(handleMultiResult(res).logFetchingEntityListWith(log))
            }
    }

    override suspend fun replace(data: List<ModifierLocal>) {
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
            delete(this.query<ModifierLocal>())
        }
    }
}