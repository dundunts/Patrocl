//package org.turter.patrocl.data.local.repository.impl.deprecated
//
//import co.touchlab.kermit.Logger
//import io.realm.kotlin.delete
//import io.realm.kotlin.ext.query
//import io.realm.kotlin.query.find
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.sync.Mutex
//import kotlinx.coroutines.sync.withLock
//import org.turter.patrocl.data.local.config.RealmManager
//import org.turter.patrocl.data.local.entity.company.CompanySourcesInfoLocal
//import org.turter.patrocl.data.local.handleSingleResult
//import org.turter.patrocl.data.local.logFetchingEntityWith
//import org.turter.patrocl.data.local.repository.CompanySourcesInfoLocalRepository
//
//class CompanySourcesInfoLocalRepositoryImpl : CompanySourcesInfoLocalRepository {
//    private val log = Logger.withTag("CompanySourcesInfoLocalRepositoryImpl")
//
//    private val realm = RealmManager.getRealm()
//
//    private val mutex = Mutex()
//
//    override fun get(): Result<CompanySourcesInfoLocal> {
//        return realm.query<CompanySourcesInfoLocal>()
//            .first()
//            .find {
//                if (it != null) Result.success(it)
//                else Result.failure(RuntimeException("Company not found"))
//            }
//    }
//
//    override fun getFlow(): Flow<Result<CompanySourcesInfoLocal>> = flow {
//        realm.query<CompanySourcesInfoLocal>()
//            .first()
//            .asFlow()
//            .collect { res ->
//                emit(handleSingleResult(res).logFetchingEntityWith(log))
//            }
//    }
//
//    override fun getById(companyId: String): Flow<Result<CompanySourcesInfoLocal>> = flow {
//        log.d { "Start getting company by company id: $companyId" }
//        realm.query<CompanySourcesInfoLocal>("companyId = $0", companyId)
//            .first()
//            .asFlow()
//            .collect { res ->
//                emit(handleSingleResult(res).logFetchingEntityWith(log))
//            }
//    }
//
//    override suspend fun setRootCategoryRkId(companyId: String, value: String) {
//        createOrUpdate(companyId) { rootCategoryRkId = value }
//    }
//
//    override suspend fun setRootModifiersGroupRkId(companyId: String, value: String) {
//        createOrUpdate(companyId) { rootModifierGroupRkId = value }
//    }
//
//    override suspend fun setDefaultHallRkId(companyId: String, value: String) {
//        createOrUpdate(companyId) { defaultHallRkId = value }
//    }
//
//    private suspend fun createOrUpdate(
//        companyId: String,
//        transform: CompanySourcesInfoLocal.() -> Unit
//    ) {
//        mutex.withLock {
//            realm.write {
//                val entity = query<CompanySourcesInfoLocal>("companyId = $0 LIMIT(1)", companyId)
//                    .first()
//                    .find()
//
//                if (entity != null) {
//                    log.d { "Update existed company: $entity" }
//                    findLatest(entity)?.apply(transform)
//                } else {
//                    log.d { "No company source info for company id: $companyId, clean up schema" }
//                    delete<CompanySourcesInfoLocal>()
//                    log.d { "Create new company source info for company id: $companyId" }
//                    copyToRealm(CompanySourcesInfoLocal.createFor(companyId).apply(transform))
//                }
//            }
//        }
////        realm.query<CompanySourcesInfoLocal>("companyId = $0 LIMIT(1)", companyId)
////            .first()
////            .find()
////            ?.also { entity ->
////                log.d { "Update existed company: $entity" }
////                realm.write {
////                    findLatest(entity)?.apply(transform)
////                }
////            }
////            ?:realm.write {
////                copyToRealm(CompanySourcesInfoLocal.createFor(companyId).apply(transform))
////            }
//    }
//}