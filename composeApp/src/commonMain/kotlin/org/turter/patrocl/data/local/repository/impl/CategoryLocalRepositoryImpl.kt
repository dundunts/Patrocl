package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.menu.CategoryLocal
import org.turter.patrocl.data.local.handleMultiResult
import org.turter.patrocl.data.local.logFetchingEntityListWith
import org.turter.patrocl.data.local.repository.CategoryLocalRepository

class CategoryLocalRepositoryImpl : CategoryLocalRepository {
    private val log = Logger.withTag("CategoryLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun count(): Long {
        log.d { "Start counting" }
        return realm.query<CategoryLocal>()
            .count()
            .find()
    }

    override fun get(): Flow<Result<List<CategoryLocal>>> = flow {
        log.d { "Start categories getAll flow" }
        realm.query<CategoryLocal>()
            .asFlow()
            .collect { res ->
                emit(handleMultiResult(res).logFetchingEntityListWith(log))
            }
    }

    override suspend fun replace(data: List<CategoryLocal>) {
        cleanUp()
        log.d { "Start writing to realm categories, total elements: ${data.size}" }
        data.forEach { element ->
            realm.write {
                copyToRealm(element)
            }
        }
        log.d { "Complete writing to realm categories, total elements: ${data.size}" }
    }

    override suspend fun cleanUp() {
        log.d { "Start cleanup categories" }
        realm.write {
            delete(this.query<CategoryLocal>())
        }
    }
}