package org.turter.patrocl.data.service

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository
import org.turter.patrocl.data.local.repository.impl.prefs.EmployeeRepositoryImpl
import org.turter.patrocl.data.remote.client.DataVersionApiClient
import org.turter.patrocl.domain.fetcher.CategoryFetcher
import org.turter.patrocl.domain.fetcher.DishFetcher
import org.turter.patrocl.domain.fetcher.HallFetcher
import org.turter.patrocl.domain.fetcher.ModifiersFetcher
import org.turter.patrocl.domain.fetcher.ModifiersGroupFetcher
import org.turter.patrocl.domain.fetcher.ModifiersSchemeFetcher
import org.turter.patrocl.domain.fetcher.OrderItemVoidFetcher
import org.turter.patrocl.domain.model.DataVersionsActualizerStatus
import org.turter.patrocl.domain.model.enums.SourceDataType
import org.turter.patrocl.domain.service.DataVersionService

class DataVersionServiceImpl(
    private val dataVersionApiClient: DataVersionApiClient,
    private val dataVersionRepository: CompanySourceDataVersionLocalRepository,
    private val employeeRepository: EmployeeRepositoryImpl,
    private val categoryFetcher: CategoryFetcher,
    private val dishFetcher: DishFetcher,
    private val modifiersFetcher: ModifiersFetcher,
    private val modifiersGroupFetcher: ModifiersGroupFetcher,
    private val modifiersSchemeFetcher: ModifiersSchemeFetcher,
    private val hallFetcher: HallFetcher,
    private val orderItemVoidFetcher: OrderItemVoidFetcher
) : DataVersionService {
    private val log = Logger.withTag("DataVersionServiceImpl")

    private val scope = CoroutineScope(Dispatchers.IO)

    private val dataVersionActualizerStatusFlow =
        MutableStateFlow<DataVersionsActualizerStatus>(DataVersionsActualizerStatus.Initial)

    private val usedSourceDataTypesFetcherMap = mapOf(
        SourceDataType.COMPANY_STATION_DISHES to dishFetcher,
        SourceDataType.COMPANY_CATEGORIES to categoryFetcher,
        SourceDataType.COMPANY_STATION_MODIFIERS to modifiersFetcher,
        SourceDataType.COMPANY_MODIFIERS_GROUPS to modifiersGroupFetcher,
        SourceDataType.COMPANY_MODIFIERS_SCHEMES to modifiersSchemeFetcher,
        SourceDataType.COMPANY_HALLS to hallFetcher,
        SourceDataType.COMPANY_ORDER_ITEM_VOIDS to orderItemVoidFetcher
    )

    override fun actualizeAndGetStatus(): StateFlow<DataVersionsActualizerStatus> {
        scope.launch { actualize() }
        return dataVersionActualizerStatusFlow.asStateFlow()
    }

    override suspend fun actualize() {
        log.d { "Start actualize source data. Status: checking" }
        dataVersionActualizerStatusFlow.value = DataVersionsActualizerStatus.Checking

        try {
            val companyId = employeeRepository.getPreferredCompanyId().first()
            if (companyId.isBlank()) throw RuntimeException("Preferred company id is blank")

            val localVersions =
                dataVersionRepository.getAllForCompanyFlow(companyId)
                    .first()
                    .getOrDefault(emptyList())

            val versionsFromApi =
                dataVersionApiClient.getDataVersionsForCompany(companyId).getOrThrow()

            log.d {
                "Actualizer: \n" +
                        " - Company id: $companyId \n" +
                        " - Local versions: $localVersions \n" +
                        " - Remote versions: $versionsFromApi"
            }

            versionsFromApi.filter { remote ->
                usedSourceDataTypesFetcherMap.contains(remote.dataType)
            }.filter { remote ->
                localVersions.none { local ->
                    local.dataType == remote.dataType.name
                            && local.version == remote.time.toString()
                            && local.companyId == remote.companyId
                }
            }.map { it.dataType }.toSet().apply {
                log.d { "Actualizer: result set of source data types: $this" }

                if (isEmpty()) {
                    log.d { "Set is empty - nothing to update. Status: completed" }
                    dataVersionActualizerStatusFlow.value = DataVersionsActualizerStatus.Completed
                    return@apply
                }

                log.d { "Set is not empty - start updating. Status: updating" }
                dataVersionActualizerStatusFlow.value = DataVersionsActualizerStatus.Updating

                this.mapNotNull { dataType -> usedSourceDataTypesFetcherMap[dataType] }
                    .toSet()
                    .forEach { fetcher ->
                        log.d { "Call refresh from remote for fetcher: $fetcher" }
                        fetcher.refreshFromRemote()
                    }

                log.d { "Actualizer: finish updating. Start checking data statuses" }

                val newVersions =
                    dataVersionRepository.getAllForCompany(companyId).getOrThrow()

                val actualCountMap = mapOf(
                    SourceDataType.COMPANY_STATION_DISHES to dishFetcher.getActualCount(),
                    SourceDataType.COMPANY_CATEGORIES to categoryFetcher.getActualCount(),
                    SourceDataType.COMPANY_STATION_MODIFIERS to modifiersFetcher.getActualCount(),
                    SourceDataType.COMPANY_MODIFIERS_GROUPS to modifiersGroupFetcher.getActualCount(),
                    SourceDataType.COMPANY_MODIFIERS_SCHEMES to modifiersSchemeFetcher.getActualCount(),
                    SourceDataType.COMPANY_HALLS to hallFetcher.getActualCount(),
                    SourceDataType.COMPANY_ORDER_ITEM_VOIDS to orderItemVoidFetcher.getActualCount()
                )
                //TODO убрать проверку кол-ва, т. к. не успевает обновиться?
                newVersions.forEach { newVersion ->
                    val remoteVersion = versionsFromApi.find { it.dataType.name == newVersion.dataType }
                    val actualCount = actualCountMap[SourceDataType.valueOf(newVersion.dataType)]
                    log.d { "New saved version for ${newVersion.dataType} \n" +
                            " - version from api: $remoteVersion \n" +
                            " - actual count: $actualCount \n" +
                            " - actual and newLocal counts equals: ${actualCount == newVersion.count} \n" +
                            " - remote and newLocal counts equals: ${remoteVersion?.count == newVersion.count} \n" +
                            " - versions equals: ${newVersion.version == remoteVersion?.time?.toString()}" }
                }
                val result = newVersions.all { newLocal ->
                    versionsFromApi.any { remote ->
                        remote.dataType.name == newLocal.dataType
                                && remote.time.toString() == newLocal.version
                                && remote.count == newLocal.count
                    }
                            && newLocal.count == actualCountMap[SourceDataType.valueOf(newLocal.dataType)]
                }


                if (result) {
                    log.d { "Actualizer: after updating all statuses are ready. Status: completed" }
                    dataVersionActualizerStatusFlow.value = DataVersionsActualizerStatus.Completed
                } else {
                    log.d { "Actualizer: after updating not all statuses are ready. Status: error" }
                    dataVersionActualizerStatusFlow.value = DataVersionsActualizerStatus.Error(
                        RuntimeException(
                            "Fail to actualize data. " +
                                    "Data statuses are not Ready after updating from remote"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            log.e("Catch exception while actualize source data", e)
            dataVersionActualizerStatusFlow.value = DataVersionsActualizerStatus.Error(e)
        }
    }
}