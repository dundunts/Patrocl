package org.turter.patrocl.data.fetcher

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.turter.patrocl.data.dto.enums.SourceDataType
import org.turter.patrocl.data.dto.source.dataversion.CompanySourceDataVersion
import org.turter.patrocl.data.local.repository.CategoryLocalRepository
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository
import org.turter.patrocl.data.local.repository.CompanySourcesInfoLocalRepository
import org.turter.patrocl.data.mapper.menu.toCategoryInfoList
import org.turter.patrocl.data.mapper.menu.toCategoryLocalList
import org.turter.patrocl.data.mapper.version.toCompanySourceDataVersionLocal
import org.turter.patrocl.data.remote.client.MenuApiClient
import org.turter.patrocl.domain.exception.EmptyMenuDataCategoryException
import org.turter.patrocl.domain.fetcher.CategoryFetcher
import org.turter.patrocl.domain.model.DataStatus
import org.turter.patrocl.domain.model.DataStatus.Empty
import org.turter.patrocl.domain.model.DataStatus.Initial
import org.turter.patrocl.domain.model.DataStatus.Loading
import org.turter.patrocl.domain.model.DataStatus.Ready
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.menu.CategoriesTreeData
import org.turter.patrocl.domain.model.menu.CategoryInfo

class CategoryFetcherImpl(
    private val menuApiClient: MenuApiClient,
    private val categoryRepository: CategoryLocalRepository,
    private val versionRepository: CompanySourceDataVersionLocalRepository,
    private val companyInfoRepository: CompanySourcesInfoLocalRepository
) : CategoryFetcher {
    private val log = Logger.withTag("CategoryFetcherImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val categoryFlow = categoryRepository
        .get()
        .map { res ->
            res.map { it.toCategoryInfoList() }
        }
        .distinctUntilChanged()

    private val rootCategoryRkIdFlow = companyInfoRepository
        .get()
        .map { res ->
            log.d { "Get company info: ${res.getOrNull()}" }
            res.map { it.rootCategoryRkId }
        }
        .distinctUntilChanged()

    private val refreshCategoryFlow = MutableSharedFlow<Unit>(replay = 1)

    private val categoryDataStatus = MutableStateFlow<DataStatus>(Initial)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryTreeStateFlow = flow<FetchState<CategoriesTreeData>> {
        log.d { "Creating categories data state flow" }
        refreshCategoryFlow.emit(Unit)
        refreshCategoryFlow.collect {
            log.d { "Categories data state flow - collect event" }
            emit(FetchState.loading())
            categoryDataStatus.emit(Loading)

            categoryFlow.flatMapLatest { current ->
                log.d { "Categories data state flow - latest categories result: $current" }
                if (current.isSuccess) {
                    log.d { "Categories is present - emit current value" }
                    flowOf(current)
                } else {
                    flow<Result<List<CategoryInfo>>> {
                        log.d { "Categories result is failure - start updating from remote" }
                        refreshFromRemote()
                        emitAll(categoryFlow)
                    }
                }
            }.combine(rootCategoryRkIdFlow) { categoriesRes, rootRkIdRes ->
                try {
                    Result.success(
                        CategoriesTreeData(
                            rootCategoryRkId = rootRkIdRes.getOrThrow(),
                            categories = categoriesRes.getOrThrow()
                        )
                    )
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }.collect { result ->
                result.fold(
                    onSuccess = {
                        emit(FetchState.done(result))
                        log.d { "Current categories is present, emit data status READY" }
                        categoryDataStatus.emit(Ready)
                    },
                    onFailure = {
                        emit(FetchState.fail(EmptyMenuDataCategoryException()))
                        log.d { "Current categories is null, emit data status EMPTY" }
                        categoryDataStatus.emit(Empty)
                    }
                )
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    override fun getStateFlow(): StateFlow<FetchState<CategoriesTreeData>> = categoryTreeStateFlow

    override fun getDataStatus(): StateFlow<DataStatus> = categoryDataStatus.asStateFlow()

    override suspend fun refresh() {
        refreshCategoryFlow.emit(Unit)
    }

    override suspend fun refreshFromRemote() {
        log.d { "Start updating categories from remote" }
        categoryDataStatus.emit(Loading)
        menuApiClient.getCategoriesForUser().fold(
            onSuccess = { categoryData ->
                log.d {
                    "Success fetching categories from remote - start replace to local data. " +
                            "CategoryDto: $categoryData"
                }
                categoryRepository.replace(categoryData.categories.toCategoryLocalList())
                log.d { "Complete replacing categories in local storage - " +
                        "start updating versions repository" }
                versionRepository.updateVersion(
                    CompanySourceDataVersion.forCategories(
                        categoryData.companyId,
                        categoryData.categories.count().toLong(),
                        categoryData.version
                    ).toCompanySourceDataVersionLocal()
                )
                log.d { "Complete updating versions repository - start updating company info " +
                        "repository: set root category rk id: ${categoryData.rootCategoryRkId}" }
                companyInfoRepository.setRootCategoryRkId(
                    categoryData.companyId,
                    categoryData.rootCategoryRkId
                )
                log.d { "Complete updating company info - emit data status Ready" }
                categoryDataStatus.emit(Ready)
            },
            onFailure = { cause ->
                log.e { "Fail fetching categories from remote - start cleanup local data" }
                categoryRepository.cleanUp()
                versionRepository.deleteVersionFor(SourceDataType.COMPANY_CATEGORIES)
                categoryDataStatus.emit(Empty)
            }
        )
    }

}