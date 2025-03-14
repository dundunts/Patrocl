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
import org.turter.patrocl.data.dto.source.dataversion.CompanySourceDataVersion
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository
import org.turter.patrocl.data.local.repository.ModifiersGroupLocalRepository
import org.turter.patrocl.data.local.repository.impl.prefs.SourceDataPrefsImpl
import org.turter.patrocl.data.mapper.menu.toModifiersGroupInfoListFromLocal
import org.turter.patrocl.data.mapper.menu.toModifiersGroupLocalList
import org.turter.patrocl.data.mapper.version.toCompanySourceDataVersionLocal
import org.turter.patrocl.data.remote.client.MenuApiClient
import org.turter.patrocl.domain.exception.EmptyMenuDataCategoryException
import org.turter.patrocl.domain.fetcher.ModifiersGroupFetcher
import org.turter.patrocl.domain.model.DataStatus
import org.turter.patrocl.domain.model.DataStatus.Empty
import org.turter.patrocl.domain.model.DataStatus.Initial
import org.turter.patrocl.domain.model.DataStatus.Loading
import org.turter.patrocl.domain.model.DataStatus.Ready
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.enums.SourceDataType
import org.turter.patrocl.domain.model.menu.ModifierGroupInfo
import org.turter.patrocl.domain.model.menu.ModifiersGroupsTreeData

class ModifiersGroupFetcherImpl(
    private val menuApiClient: MenuApiClient,
    private val modifiersGroupRepository: ModifiersGroupLocalRepository,
    private val dataVersionRepository: CompanySourceDataVersionLocalRepository,
    private val sourceDataPrefs: SourceDataPrefsImpl
//    private val companyRepository: CompanySourcesInfoLocalRepository
) : ModifiersGroupFetcher {
    private val log = Logger.withTag("ModifiersGroupFetcherImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val modifiersGroupFlow = modifiersGroupRepository
        .get()
        .map { res ->
            res.map { it.toModifiersGroupInfoListFromLocal() }
        }
        .distinctUntilChanged()

    private val rootModifiersGroupRkIdFlow = sourceDataPrefs
        .getRootModifierGroupRkId()
        .distinctUntilChanged()

    private val refreshModifiersGroupFlow = MutableSharedFlow<Unit>(replay = 1)

    private val modifiersGroupDataStatus = MutableStateFlow<DataStatus>(Initial)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val modifiersGroupTreeStateFlow = flow<FetchState<ModifiersGroupsTreeData>> {
        log.d { "Creating modifiers group tree state flow" }
        refreshModifiersGroupFlow.emit(Unit)
        refreshModifiersGroupFlow.collect {
            log.d { "Modifiers group tree state flow - collect event" }
            emit(FetchState.loading())
            modifiersGroupDataStatus.emit(Loading)

            modifiersGroupFlow.flatMapLatest { current ->
                log.d { "Modifiers group tree state flow - latest modifiers group result: $current" }
                if (current.isSuccess) {
                    log.d { "Modifiers group is present - emit current value" }
                    flowOf(current)
                } else {
                    flow<Result<List<ModifierGroupInfo>>> {
                        log.d { "Modifiers group result is failure - start updating from remote" }
                        refreshFromRemote()
                        emitAll(modifiersGroupFlow)
                    }
                }
            }.combine(rootModifiersGroupRkIdFlow) { groupsRes, rootRkIdRes ->
                try {
                    if (rootRkIdRes.isBlank()) throw RuntimeException("Root modifiers group rk id is blank")
                    Result.success(
                        ModifiersGroupsTreeData(
                            rootGroupRkId = rootRkIdRes,
                            groups = groupsRes.getOrThrow()
                        )
                    )
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }.collect { result ->
                result.fold(
                    onSuccess = {
                        emit(FetchState.done(result))
                        log.d { "Current modifiers group is present, emit data status READY" }
                        modifiersGroupDataStatus.emit(Ready)
                    },
                    onFailure = {
                        emit(FetchState.fail(EmptyMenuDataCategoryException()))
                        log.d { "Current modifiers group is null, emit data status EMPTY" }
                        modifiersGroupDataStatus.emit(Empty)
                    }
                )
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    override fun getStateFlow(): StateFlow<FetchState<ModifiersGroupsTreeData>> =
        modifiersGroupTreeStateFlow

    override fun getDataStatus(): StateFlow<DataStatus> = modifiersGroupDataStatus.asStateFlow()

    override fun getActualCount(): Long {
        return modifiersGroupRepository.count()
    }

    override suspend fun refresh() {
        refreshModifiersGroupFlow.emit(Unit)
    }

    override suspend fun refreshFromRemote() {
        log.d { "Start updating modifiers group from remote" }
        modifiersGroupDataStatus.emit(Loading)
        menuApiClient.getModifiersGroupsForUser().fold(
            onSuccess = { res ->
                val companyId = res.companyId
                val groups = res.modifiersGroups
                log.d {
                    "Success fetching modifiers groups from remote - start replace to local data. " +
                            "Groups count: ${groups.size}"
                }
                modifiersGroupRepository.replace(groups.toModifiersGroupLocalList())
                dataVersionRepository.updateVersion(
                    CompanySourceDataVersion.forModifiersGroup(
                        companyId,
                        groups.size.toLong(),
                        res.version
                    ).toCompanySourceDataVersionLocal()
                )
                sourceDataPrefs.setRootModifierGroupRkId(res.rootModifiersGroupId)
                modifiersGroupDataStatus.emit(Ready)
            },
            onFailure = { cause ->
                log.e {
                    "Fail fetching modifiers group from remote - start cleanup local data. " +
                            "Cause: $cause"
                }
                modifiersGroupRepository.cleanUp()
                dataVersionRepository.deleteVersionFor(SourceDataType.COMPANY_MODIFIERS_GROUPS)
                modifiersGroupDataStatus.emit(Empty)
            }
        )
    }
}