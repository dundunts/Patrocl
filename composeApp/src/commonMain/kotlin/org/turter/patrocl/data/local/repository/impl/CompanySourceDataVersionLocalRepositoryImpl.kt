package org.turter.patrocl.data.local.repository.impl

import co.touchlab.kermit.Logger
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.find
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.turter.patrocl.domain.model.enums.SourceDataType
import org.turter.patrocl.data.local.config.RealmManager
import org.turter.patrocl.data.local.entity.version.CompanySourceDataVersionLocal
import org.turter.patrocl.data.local.handleMultiResult
import org.turter.patrocl.data.local.handleNullableResult
import org.turter.patrocl.data.local.handleSingleResult
import org.turter.patrocl.data.local.logFetchingEntityListWith
import org.turter.patrocl.data.local.logFetchingEntityWith
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository

class CompanySourceDataVersionLocalRepositoryImpl : CompanySourceDataVersionLocalRepository {
    private val log = Logger.withTag("CompanySourceDataVersionLocalRepositoryImpl")

    private val realm = RealmManager.getRealm()

    override fun getAllForCompany(companyId: String): Result<List<CompanySourceDataVersionLocal>> {
        log.d { "Start get versions by company id" }
        return try {
            Result.success(
                realm.query<CompanySourceDataVersionLocal>("companyId = $0", companyId)
                    .find()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllForCompanyFlow(companyId: String): Flow<Result<List<CompanySourceDataVersionLocal>>> =
        flow {
            log.d { "Start get versions by company id flow" }
            realm.query<CompanySourceDataVersionLocal>("companyId = $0", companyId)
                .asFlow()
                .collect { res ->
                    emit(handleMultiResult(res).logFetchingEntityListWith(log))
                }
        }

    override fun getForSource(
        dataType: SourceDataType,
        companyId: String
    ): Result<CompanySourceDataVersionLocal> {
        return realm.query<CompanySourceDataVersionLocal>(
            "dataType == $0 AND companyId == $1 LIMIT(1)",
            dataType.name,
            companyId
        )
            .first()
            .find { it.handleNullableResult() }
    }

    override fun getForSourceFlow(
        dataType: SourceDataType,
        companyId: String
    ): Flow<Result<CompanySourceDataVersionLocal>> =
        flow {
            log.d { "Start get version by data type flow" }
            realm.query<CompanySourceDataVersionLocal>(
                "dataType == $0 AND companyId == $1 LIMIT(1)",
                dataType.name,
                companyId
            )
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