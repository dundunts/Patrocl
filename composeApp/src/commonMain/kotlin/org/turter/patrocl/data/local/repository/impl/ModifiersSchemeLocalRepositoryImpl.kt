package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.menu.ModifiersSchemeDetailsLocal
import org.turter.patrocl.data.local.entity.menu.ModifiersSchemeLocal
import org.turter.patrocl.data.local.handleMultiResult
import org.turter.patrocl.data.local.logFetchingEntityListWith
import org.turter.patrocl.data.local.repository.ModifiersSchemeLocalRepository

class ModifiersSchemeLocalRepositoryImpl : ModifiersSchemeLocalRepository {
    private val log = Logger.withTag("ModifiersSchemeLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun count(): Long {
        log.d { "Start counting" }
        return realm.query<ModifiersSchemeLocal>()
            .count()
            .find()
    }

    override fun countDetails(): Long {
        log.d { "Start counting details" }
        return realm.query<ModifiersSchemeLocal>()
            .find()
            .sumOf { it.details.size.toLong() }
    }

    override fun get(): Flow<Result<List<ModifiersSchemeLocal>>> = flow {
        log.d { "Start Entity getAll flow" }
        realm.query<ModifiersSchemeLocal>()
            .asFlow()
            .collect { res ->
                emit(handleMultiResult(res).logFetchingEntityListWith(log))
            }
    }

    override suspend fun replace(data: List<ModifiersSchemeLocal>) {
        cleanUp()
        log.d { "Start writing to realm modifiers schemes and details, total elements: ${data.size}" }
        data.forEach { element ->
            realm.write {
                copyToRealm(element)
            }
        }
        log.d { "Complete writing to realm modifiers scheme and details, total elements: ${data.size}" }
    }

    override suspend fun cleanUp() {
        log.d { "Start cleanup Entity" }
        realm.write {
            delete(this.query<ModifiersSchemeLocal>())
            delete(this.query<ModifiersSchemeDetailsLocal>())
        }
    }
}