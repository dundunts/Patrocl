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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.turter.patrocl.data.dto.source.dataversion.CompanySourceDataVersion
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository
import org.turter.patrocl.data.local.repository.ModifiersSchemeLocalRepository
import org.turter.patrocl.data.mapper.menu.toModifiersSchemeInfoListFromLocal
import org.turter.patrocl.data.mapper.menu.toModifiersSchemeLocalList
import org.turter.patrocl.data.mapper.version.toCompanySourceDataVersionLocal
import org.turter.patrocl.data.remote.client.MenuApiClient
import org.turter.patrocl.domain.exception.EmptyMenuDataModifiersException
import org.turter.patrocl.domain.fetcher.ModifiersSchemeFetcher
import org.turter.patrocl.domain.model.DataStatus
import org.turter.patrocl.domain.model.DataStatus.Empty
import org.turter.patrocl.domain.model.DataStatus.Initial
import org.turter.patrocl.domain.model.DataStatus.Loading
import org.turter.patrocl.domain.model.DataStatus.Ready
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.enums.SourceDataType
import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo

class ModifiersSchemeFetcherImpl(
    private val menuApiClient: MenuApiClient,
    private val modifiersSchemesRepository: ModifiersSchemeLocalRepository,
    private val dataVersionRepository: CompanySourceDataVersionLocalRepository
) : ModifiersSchemeFetcher {
    private val log = Logger.withTag("ModifiersSchemeFetcherImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val modifiersSchemeFlow = modifiersSchemesRepository
        .get()
        .map { res ->
            res.map { it.toModifiersSchemeInfoListFromLocal() }
        }
        .distinctUntilChanged()

    private val refreshModifiersSchemesFlow = MutableSharedFlow<Unit>(replay = 1)

    private val modifiersSchemesDataStatus = MutableStateFlow<DataStatus>(Initial)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val modifiersSchemesStateFlow = flow<FetchState<List<ModifierSchemeInfo>>> {
        log.d { "Creating modifiers schemes state flow" }
        refreshModifiersSchemesFlow.emit(Unit)
        refreshModifiersSchemesFlow.collect {
            log.d { "Modifiers schemes state flow - collect event" }
            emit(FetchState.loading())
            modifiersSchemesDataStatus.emit(Loading)

            modifiersSchemeFlow.flatMapLatest { current ->
                log.d { "Modifiers schemes state flow - latest modifiers schemes list result: $current" }
                if (current.isSuccess) {
                    log.d { "Modifiers schemes is present - emit current value" }
                    flowOf(current)
                } else {
                    flow<Result<List<ModifierSchemeInfo>>> {
                        log.d { "Modifiers schemes result is failure - start updating from remote" }
                        refreshFromRemote()
                        emitAll(modifiersSchemeFlow)
                    }
                }
            }.collect { result ->
                result.fold(
                    onSuccess = {
                        emit(FetchState.done(result))
                        log.d { "Current modifiers schemes is present, emit data status READY" }
                        modifiersSchemesDataStatus.emit(Ready)
                    },
                    onFailure = {
                        emit(FetchState.fail(EmptyMenuDataModifiersException()))
                        log.d { "Current modifiers schemes is empty, emit data status EMPTY" }
                        modifiersSchemesDataStatus.emit(Empty)
                    }
                )
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    override fun getStateFlow(): StateFlow<FetchState<List<ModifierSchemeInfo>>> =
        modifiersSchemesStateFlow

    override fun getDataStatus(): StateFlow<DataStatus> = modifiersSchemesDataStatus.asStateFlow()

    override fun getActualCount(): Long {
        return modifiersSchemesRepository.count()
    }

    override fun getActualDetailsCount(): Long {
        return modifiersSchemesRepository.countDetails()
    }

    override suspend fun refresh() {
        refreshModifiersSchemesFlow.emit(Unit)
    }

    override suspend fun refreshFromRemote() {
        log.d { "Start updating modifiers from remote" }
        modifiersSchemesDataStatus.emit(Loading)
        menuApiClient.getModifiersSchemesForUser().fold(
            onSuccess = { res ->
                val schemes = res.modifiersSchemes
                log.d {
                    "Success fetching modifiers schemes from remote - start replace to local data. " +
                            "Modifiers schemes list size: ${schemes.size}"
                }
                modifiersSchemesRepository.replace(schemes.toModifiersSchemeLocalList())
                dataVersionRepository.updateVersion(
                    CompanySourceDataVersion.forModifiersScheme(
                        res.companyId,
                        schemes.size.toLong(),
                        res.schemesVersion
                    ).toCompanySourceDataVersionLocal()
                )
//                dataVersionRepository.updateVersion(
//                    CompanySourceDataVersion.forModifiersSchemeDetails(
//                        res.companyId,
//                        schemes.flatMap { it.details }.count().toLong(),
//                        res.schemesDetailsVersion
//                    ).toCompanySourceDataVersionLocal()
//                )
                modifiersSchemesDataStatus.emit(Ready)
            },
            onFailure = { cause ->
                log.e { "Fail fetching modifiers from remote - start cleanup local data" }
                modifiersSchemesRepository.cleanUp()
                dataVersionRepository.deleteVersionFor(SourceDataType.COMPANY_MODIFIERS_SCHEMES)
//                dataVersionRepository.deleteVersionFor(SourceDataType.COMPANY_MODIFIERS_SCHEMES_DETAILS)
                modifiersSchemesDataStatus.emit(Empty)
            }
        )
    }
}