package org.turter.patrocl.di

import cafe.adriel.voyager.navigator.Navigator
import org.koin.dsl.module
import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.menu.MenuTreeData
import org.turter.patrocl.domain.model.menu.deprecated.MenuData
import org.turter.patrocl.domain.model.order.NewOrderItem
import org.turter.patrocl.domain.model.person.Waiter
import org.turter.patrocl.domain.model.stoplist.StopListItem
import org.turter.patrocl.presentation.auth.AuthViewModel
import org.turter.patrocl.presentation.main.MainViewModel
import org.turter.patrocl.presentation.orders.create.CreateOrderViewModel
import org.turter.patrocl.presentation.orders.common.OrderInfo
import org.turter.patrocl.presentation.orders.edit.EditOrderViewModel
import org.turter.patrocl.presentation.orders.info.UpdateOrderInfoViewModel
import org.turter.patrocl.presentation.orders.item.new.edit.EditNewOrderItemViewModel
import org.turter.patrocl.presentation.orders.item.new.modifiers.SelectModifiersViewModel
import org.turter.patrocl.presentation.orders.list.OrdersViewModel
import org.turter.patrocl.presentation.orders.read.ReadOrderViewModel
import org.turter.patrocl.presentation.profile.ProfileViewModel
import org.turter.patrocl.presentation.stoplist.create.CreateStopListItemViewModel
import org.turter.patrocl.presentation.stoplist.edit.EditStopListItemViewModel
import org.turter.patrocl.presentation.stoplist.list.StopListViewModel

val viewModelModule = module {
    factory { (postLoginNav: () -> Unit) ->
        AuthViewModel(
            authService = get(),
            postLoginNav = postLoginNav
        )
    }

    factory {
        MainViewModel(
            authService = get(),
            waiterService = get(),
            messageService = get()
        )
    }

    factory { OrdersViewModel(orderService = get(), waiterService = get()) }

    factory { (
                  navigateToModifiersSelector: (
                      item: NewOrderItem, menuData: MenuTreeData,
                      autoOpened: Boolean, onSave: (item: NewOrderItem) -> Unit
                  ) -> Unit,
                  navigateToUpdateOrderInfoScreen: (
                      info: OrderInfo,
                      halls: HallsData,
                      availableWaiters: List<Waiter>,
                      onSave: (info: OrderInfo) -> Result<Unit>
                  ) -> Unit,
                  navigateToOrder: (orderGuid: String) -> Unit,
                  navigateBack: () -> Unit
              ) ->
        CreateOrderViewModel(
            menuService = get(),
            hallFetcher = get(),
            waiterService = get(),
            orderService = get(),
            navigateToModifiersSelector = navigateToModifiersSelector,
            navigateToUpdateOrderInfoScreen = navigateToUpdateOrderInfoScreen,
            navigateToOrder = navigateToOrder,
            navigateBack = navigateBack
        )
    }

    factory { (
                  orderGuid: String,
                  navigateToModifiersSelector: (
                      item: NewOrderItem, menuData: MenuTreeData,
                      autoOpened: Boolean, onSave: (item: NewOrderItem) -> Unit
                  ) -> Unit,
                  navigateToUpdateOrderInfoScreen: (
                      info: OrderInfo,
                      halls: HallsData,
                      availableWaiters: List<Waiter>,
                      onSave: (info: OrderInfo) -> Result<Unit>
                  ) -> Unit,
                  navigateBack: () -> Unit
    ) ->
        EditOrderViewModel(
            orderGuid = orderGuid,
            menuService = get(),
            hallFetcher = get(),
            waiterService = get(),
            orderService = get(),
            navigateToModifiersSelector = navigateToModifiersSelector,
            navigateToUpdateOrderInfoScreen = navigateToUpdateOrderInfoScreen,
            navigateBack = navigateBack
        )
    }

    factory { (orderGuid: String, navigator: Navigator) ->
        ReadOrderViewModel(
            orderGuid = orderGuid,
            orderService = get(),
            navigator = navigator
        )
    }

    factory { (originalItem: NewOrderItem,
                  menuData: MenuData,
                  onSave: (NewOrderItem) -> Unit,
                  onDelete: (NewOrderItem) -> Unit) ->
        EditNewOrderItemViewModel(
            originalItem = originalItem,
            menuData = menuData,
            onSave = onSave,
            onDelete = onDelete
        )
    }

    factory {
        ProfileViewModel(
            waiterService = get(),
            employeeService = get(),
            menuService = get(),
            hallFetcher = get(),
            authService = get()
        )
    }

    factory {
        StopListViewModel(stopListService = get())
    }

    factory { (currentStopList: List<StopListItem>) ->
        CreateStopListItemViewModel(
            currentStopList = currentStopList,
            dishFetcher = get(),
            stopListService = get()
        )
    }

    factory { (targetItem: StopListItem) ->
        EditStopListItemViewModel(
            targetItem = targetItem,
            stopListService = get()
        )
    }

    factory { (originalItem: NewOrderItem, menuTreeData: MenuTreeData, autoOpened: Boolean,
                  onSave: (item: NewOrderItem) -> Unit, onClose: () -> Unit) ->
        SelectModifiersViewModel(
            originalItem = originalItem,
            menuData = menuTreeData,
            autoOpened = autoOpened,
            onSave = onSave,
            onClose = onClose
        )
    }

    factory { (info: OrderInfo, halls: HallsData, availableWaiters: List<Waiter>,
                  onSave: (info: OrderInfo) -> Result<Unit>, navigateBack: () -> Unit) ->
        UpdateOrderInfoViewModel(
            initInfo = info,
            halls = halls,
            availableWaiters = availableWaiters,
            onSave = onSave,
            navigateBack = navigateBack
        )
    }
}