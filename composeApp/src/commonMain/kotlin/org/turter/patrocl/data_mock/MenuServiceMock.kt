package org.turter.patrocl.data_mock

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.turter.patrocl.data_mock.utils.MenuDataSupplier
import org.turter.patrocl.data_mock.utils.OrderItemVoidDataSupplier
import org.turter.patrocl.data_mock.utils.StopListDataSupplier
import org.turter.patrocl.domain.model.DataStatus
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

class MenuServiceMock : MenuService {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val dishesFlow = flowOf(FetchState.success(MenuDataSupplier.getDishList()))
    private val categoryFlow = flowOf(FetchState.success(MenuDataSupplier.getCategoryTree()))
    private val modifiersFlow = flowOf(FetchState.success(MenuDataSupplier.getModifierList()))
    private val modifiersGroupFlow =
        flowOf(FetchState.success(MenuDataSupplier.getModifiersGroupTree()))
    private val modifiersSchemeFlow = flowOf(FetchState.success(MenuDataSupplier.getModifierScheme()))
    private val orderItemVoidFlow = flowOf(FetchState.success(OrderItemVoidDataSupplier.getOrderItemsVoids()))
    private val stopListFlow = flowOf(FetchState.success(StopListDataSupplier.getStopList()))

    private val _menuDataStatusStateFlow = MutableStateFlow<DataStatus>(DataStatus.Initial)

    private val menuStateFlow = flow<FetchState<MenuTreeData>> {
        org.turter.patrocl.utils.combine(
            dishesFlow,
            categoryFlow,
            modifiersFlow,
            modifiersGroupFlow,
            modifiersSchemeFlow,
            orderItemVoidFlow,
            stopListFlow
        ) { dishes, categories, modis, modiGroups, modiSchemes, voids, stopList ->
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
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = FetchState.initial()
    )

    override fun getMenuTreeDataStateFlow(): StateFlow<FetchState<MenuTreeData>> =
        menuStateFlow

    override fun getMenuTreeDataStatusStateFlow(): StateFlow<DataStatus> =
        _menuDataStatusStateFlow.asStateFlow()

    override suspend fun refreshMenu() {

    }

    override suspend fun refreshMenuFromApi() {

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
        return MenuTreeData(
            rootCategoryRkId = categoriesTree.rootCategoryRkId,
            dishRkIdMap = dishes.associateBy { it.rkId },
            categoryRkIdMap = categoriesTree.categories.associateBy { it.rkId },
            rootModifierGroupRkId = modifiersGroupTree.rootGroupRkId,
            modifiersGroupRkIdMap = modifiersGroupTree.groups.associateBy { it.rkId },
            modifiersRkIdMap = modifiers.associateBy { it.rkId },
            modifiersSchemeRkIdMap = modifiersSchemes.associateBy { it.rkId },
            orderItemVoids = voids,
            stopListDishRkIdMap = stopList.associateBy { it.dishId }
        )
    }

}