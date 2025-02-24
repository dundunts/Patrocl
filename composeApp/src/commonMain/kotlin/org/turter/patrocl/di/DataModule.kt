package org.turter.patrocl.di

import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect

@OptIn(ExperimentalOpenIdConnect::class)
val dataModule = module {
//    single<HttpClient> {
//        val initializer: HttpClientInitializer = get()
//        initializer.defaultHttpClient
//    }
//
//    single<OpenIdConnectClient> {
//        val initializer: OidcClientInitializer = get()
//        initializer.client
//    }
//
//    single<AuthService> {
//        AuthServiceImpl(
//            appAuth = get(),
//            httpClient = get(),
//            tokenStore = get(),
//            ownWaiterLocalRepository = get(),
//            employeeLocalSource = get(named("employeeLocalSource")),
//            employeeService = get(),
//            waiterService = get()
//        )
//    }
//
////    single<WebSocketFlowFactory> {
////        WebSocketFlowFactory(httpClient = get(), authService = get())
////    }
//
//    single<MessageService> { MessageServiceImpl() }
//
//    single<MenuService> {
//        MenuServiceImpl(
//            categoryFetcher = get(),
//            modifiersFetcher = get(),
//            modifiersGroupFetcher = get(),
//            dishesFetcher = get(),
//            stopListService = get()
//        )
//    }
//
//    single<StopListService> {
//        StopListServiceImpl(
//            stopListApiClient = get(),
//            dishFetcher = get(),
//            messageService = get()
//        )
//    }
//
//    single<TableService> {
//        TableServiceImpl(
//            hallApiClient = get(),
//            tableLocalSource = get(named("tableLocalSource"))
//        )
//    }
//
//    single<WaiterService> {
//        WaiterServiceImpl(waiterApiClient = get(), ownWaiterLocalRepository = get())
//    }
//
//    single<EmployeeService> {
//        EmployeeServiceImpl(
//            employeeApiClient = get(),
//            employeeLocalSource = get(named("employeeLocalSource"))
//        )
//    }
//
//    single<OrderService> { OrderServiceImpl(orderApiClient = get(), messageService = get()) }
//
//    single<MenuApiClient> { MenuApiClientImpl(httpClient = get()) }
//
//    single<HallApiClient> { HallApiClientImpl(httpClient = get()) }
//
//    single<OrderApiClient> { OrderApiClientImpl(httpClient = get()) }
//
//    single<WaiterApiClient> { WaiterApiClientImpl(httpClient = get()) }
//
//    single<EmployeeApiClient> { EmployeeApiClientImpl(httpClient = get()) }
//
//    single<StopListApiClient> { StopListApiClientImpl(httpClient = get()) }
//
//    single<OwnWaiterLocalRepository> { OwnWaiterLocalRepositoryImpl() }
//
//    single<LocalSource<CategoryLocal>>(named("categoryLocalSource")) { CategoryLocalSource() }
//
//    single<LocalSource<List<DishLocal>>>(named("dishLocalSource")) { DishLocalRepositoryImpl() }
//
//    single<LocalSource<ModifiersGroupLocal>>(named("modifiersGroupLocalSource")) { ModifiersGroupLocalRepositoryImpl() }
//
//    single<LocalSource<List<ModifierLocal>>>(named("modifiersLocalSource")) { ModifierLocalRepositoryImpl() }
//
//    single<LocalSource<List<TableLocal>>>(named("tableLocalSource")) { HallLocalRepositoryImpl() }
//
//    single<LocalSource<EmployeeLocal>>(named("employeeLocalSource")) { EmployeeLocalRepositoryImpl() }
//
//    single<CategoryFetcher> {
//        CategoryFetcherImpl(
//            menuApiClient = get(),
//            categoryRepository = get(named("categoryLocalSource"))
//        )
//    }
//
//    single<DishFetcher> {
//        DishFetcherImpl(
//            menuApiClient = get(),
//            dishRepository = get(named("dishLocalSource"))
//        )
//    }
//
//    single<ModifiersGroupFetcher> {
//        ModifiersGroupFetcherImpl(
//            menuApiClient = get(),
//            localSource = get(named("modifiersGroupLocalSource"))
//        )
//    }
//
//    single<ModifiersFetcher> {
//        ModifiersFetcherImpl(
//            menuApiClient = get(),
//            modifiersRepository = get(named("modifiersLocalSource"))
//        )
//    }
//
//    single<HallFetcher> {
//        HallFetcherImpl(
//            hallApiClient = get(),
//            tableLocalSource = get(named("tableLocalSource"))
//        )
//    }

}