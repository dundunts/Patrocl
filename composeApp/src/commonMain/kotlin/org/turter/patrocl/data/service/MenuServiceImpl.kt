package org.turter.patrocl.data.service

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.turter.patrocl.domain.exception.ComponentMenuErrorException
import org.turter.patrocl.domain.fetcher.CategoryFetcher
import org.turter.patrocl.domain.fetcher.DishFetcher
import org.turter.patrocl.domain.fetcher.ModifiersFetcher
import org.turter.patrocl.domain.fetcher.ModifiersGroupFetcher
import org.turter.patrocl.domain.fetcher.ModifiersSchemeFetcher
import org.turter.patrocl.domain.fetcher.OrderItemVoidFetcher
import org.turter.patrocl.domain.model.DataStatus
import org.turter.patrocl.domain.model.DataStatus.Empty
import org.turter.patrocl.domain.model.DataStatus.Error
import org.turter.patrocl.domain.model.DataStatus.Initial
import org.turter.patrocl.domain.model.DataStatus.Loading
import org.turter.patrocl.domain.model.DataStatus.Ready
import org.turter.patrocl.domain.model.FetchState
import org.turter.patrocl.domain.model.FetchState.Finished
import org.turter.patrocl.domain.model.menu.CategoriesTreeData
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo
import org.turter.patrocl.domain.model.menu.ModifiersGroupsTreeData
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.menu.StationModifierInfo
import org.turter.patrocl.domain.model.stoplist.StopListItem
import org.turter.patrocl.domain.model.voids.OrderItemVoidInfo
import org.turter.patrocl.domain.service.MenuService
import org.turter.patrocl.domain.service.StopListService
import org.turter.patrocl.utils.combine

class MenuServiceImpl(
    private val dishesFetcher: DishFetcher,
    private val categoryFetcher: CategoryFetcher,
    private val modifiersFetcher: ModifiersFetcher,
    private val modifiersGroupFetcher: ModifiersGroupFetcher,
    private val modifiersSchemeFetcher: ModifiersSchemeFetcher,
    private val orderItemVoidFetcher: OrderItemVoidFetcher,
    private val stopListService: StopListService
) : MenuService {
    private val log = Logger.withTag("MenuServiceImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val menuDataStatus = flow<DataStatus> {
        combine(
            dishesFetcher.getDataStatus(),
            categoryFetcher.getDataStatus(),
            modifiersFetcher.getDataStatus(),
            modifiersGroupFetcher.getDataStatus(),
            modifiersSchemeFetcher.getDataStatus(),
            orderItemVoidFetcher.getDataStatus()
        ) { dishes, cat, modis, modisGroup, modiSchemes, voids ->
            if (dishes is Ready && cat is Ready && modis is Ready && modisGroup is Ready
                && modiSchemes is Ready && voids is Ready) Ready
            else if (dishes is Error || cat is Error || modis is Error || modisGroup is Error
                || modiSchemes is Error || voids is Error) Error(ComponentMenuErrorException())
            else if (dishes is Empty || cat is Empty || modis is Empty || modisGroup is Empty
                || modiSchemes is Empty || voids is Empty) Empty
            else if (dishes is Initial || cat is Initial || modis is Initial
                || modisGroup is Initial || modiSchemes is Initial || voids is Initial) Initial
            else Loading
        }.collect { newValue ->
            emit(newValue)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = Initial
    )

    private val menuDataStateFlow = flow<FetchState<MenuTreeData>> {
        combine(
            dishesFetcher.getStateFlow(),
            categoryFetcher.getStateFlow(),
            modifiersFetcher.getStateFlow(),
            modifiersGroupFetcher.getStateFlow(),
            modifiersSchemeFetcher.getStateFlow(),
            orderItemVoidFetcher.getStateFlow(),
            stopListService.getStopListStateFlow()
        ) { dishes, categories, modis, modiGroups, modiSchemes, voids, stopList ->
            log.d {
                "Combine flows to menu data flow: \n" +
                    "- dishes: $dishes \n" +
                    "- category: $categories \n" +
                    "- modifiers: $modis \n" +
                    "- modifiersGroup: $modiGroups \n" +
                    "- modiSchemes: $modiSchemes \n" +
                    "- voids: $voids \n" +
                    "- stopList: $stopList \n"
            }
            if (
                categories is Finished
                && dishes is Finished
                && modis is Finished
                && modiGroups is Finished
                && modiSchemes is Finished
                && voids is Finished
                && stopList is Finished
            ) {
                try {
                    FetchState.success(
                        buildMenuTreeData(
                            dishes = dishes.result.getOrThrow(),
                            categoriesTree = categories.result.getOrThrow(),
                            modifiers = modis.result.getOrThrow(),
                            modifiersGroupTree = modiGroups.result.getOrThrow(),
                            modifiersSchemes = modiSchemes.result.getOrThrow(),
                            voids = voids.result.getOrThrow(),
                            stopList = stopList.result.getOrThrow().items
                        )
                    )
                } catch (e: Exception) {
                    log.e { "Catch exception in results of flows. Exception: $e" }
                    e.printStackTrace()
                    FetchState.fail(e)
                }
            } else {
                FetchState.loading()
            }
        }.collect { fetchState ->
            emit(fetchState)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    override fun getMenuTreeDataStateFlow(): StateFlow<FetchState<MenuTreeData>> =
        menuDataStateFlow

    override fun getMenuTreeDataStatusStateFlow(): StateFlow<DataStatus> =
        menuDataStatus

    override suspend fun refreshMenu() {
        categoryFetcher.refresh()
        modifiersGroupFetcher.refresh()
        dishesFetcher.refresh()
        modifiersFetcher.refresh()
    }

    override suspend fun refreshMenuFromApi() {
        coroutineScope {
            awaitAll(
                async { dishesFetcher.refreshFromRemote() },
                async { categoryFetcher.refreshFromRemote() },
                async { modifiersFetcher.refreshFromRemote() },
                async { modifiersGroupFetcher.refreshFromRemote() },
                async { modifiersSchemeFetcher.refreshFromRemote() },
                async { orderItemVoidFetcher.refreshFromRemote() }
            )
        }
    }

    private fun buildMenuTreeData(
        dishes: List<StationDishInfo>,
        categoriesTree: CategoriesTreeData,
        modifiers: List<StationModifierInfo>,
        modifiersGroupTree: ModifiersGroupsTreeData,
        modifiersSchemes: List<ModifierSchemeInfo>,
        voids: List<OrderItemVoidInfo>,
        stopList: List<StopListItem>
    ): MenuTreeData {
        log.d(
            "Start collect menu data for: \n" +
                    " - dishes: $dishes \n" +
                    " - categoriesTree: $categoriesTree \n" +
                    " - modifiers: $modifiers \n" +
                    " - modifiersGroupTree: $modifiersGroupTree \n" +
                    " - modifiersSchemes: $modifiersSchemes \n" +
                    " - voids: $voids \n" +
                    " - stopList: $stopList"
        )
        return MenuTreeData(
            rootCategoryRkId = categoriesTree.rootCategoryRkId,
            dishRkIdMap = dishes.associateBy { it.rkId },
            categoryRkIdMap = categoriesTree.categories.associateBy { it.rkId },
            rootModifierGroupRkId = modifiersGroupTree.rootGroupRkId,
            modifiersGroupRkIdMap = modifiersGroupTree.groups.associateBy { it.rkId },
            modifiersRkIdMap = modifiers.associateBy { it.rkId },
            modifiersSchemeRkIdMap = modifiersSchemes.associateBy { it.rkId },
            orderItemVoids = voids,
            stopListDishRkIdMap = stopList.associateBy { it.dishRkId }
        )
    }
}