package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.data.dto.enums.SourceDataType
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.version.CompanySourceDataVersionLocal
import org.turter.patrocl.data.local.handleSingleResult
import org.turter.patrocl.data.local.logFetchingEntityWith
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository
import org.turter.patrocl.domain.exception.EmptyLocalDataException

class CompanySourceDataVersionLocalRepositoryImpl : CompanySourceDataVersionLocalRepository {
    private val log = Logger.withTag("CompanySourceDataVersionLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun getForSource(dataType: SourceDataType): Flow<Result<CompanySourceDataVersionLocal>> =
        flow {
            log.d { "Start Entity getAll flow" }
            realm.query<CompanySourceDataVersionLocal>("dataType = $0 LIMIT(1)", dataType.name)
                .first()
                .asFlow()
                .collect { res ->
                    emit(handleSingleResult(res).logFetchingEntityWith(log))
                }
        }

    override suspend fun updateVersion(entity: CompanySourceDataVersionLocal) {
        realm.write {
            copyToRealm(instance = entity, updatePolicy = UpdatePolicy.ALL)
        }
    }

    override suspend fun deleteVersionFor(dataType: SourceDataType) {
        realm.write {
            delete(
                this.query<CompanySourceDataVersionLocal>(
                    "dataType = $0 LIMIT(1)",
                    dataType.name
                )
            )
        }
    }
}