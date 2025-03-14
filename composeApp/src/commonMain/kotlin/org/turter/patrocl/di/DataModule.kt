package org.turter.patrocl.di

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.turter.patrocl.data.auth.OidcClientInitializer
import org.turter.patrocl.data.fetcher.CategoryFetcherImpl
import org.turter.patrocl.data.fetcher.DishFetcherImpl
import org.turter.patrocl.data.fetcher.HallFetcherImpl
import org.turter.patrocl.data.fetcher.ModifiersFetcherImpl
import org.turter.patrocl.data.fetcher.ModifiersGroupFetcherImpl
import org.turter.patrocl.data.fetcher.ModifiersSchemeFetcherImpl
import org.turter.patrocl.data.fetcher.OrderItemVoidFetcherImpl
import org.turter.patrocl.data.local.repository.CategoryLocalRepository
import org.turter.patrocl.data.local.repository.CompanySourceDataVersionLocalRepository
import org.turter.patrocl.data.local.repository.DishLocalRepository
import org.turter.patrocl.data.local.repository.HallLocalRepository
import org.turter.patrocl.data.local.repository.ModifierLocalRepository
import org.turter.patrocl.data.local.repository.ModifiersGroupLocalRepository
import org.turter.patrocl.data.local.repository.ModifiersSchemeLocalRepository
import org.turter.patrocl.data.local.repository.OrderItemVoidLocalRepository
import org.turter.patrocl.data.local.repository.impl.CategoryLocalRepositoryImpl
import org.turter.patrocl.data.local.repository.impl.CompanySourceDataVersionLocalRepositoryImpl
import org.turter.patrocl.data.local.repository.impl.DishLocalRepositoryImpl
import org.turter.patrocl.data.local.repository.impl.HallLocalRepositoryImpl
import org.turter.patrocl.data.local.repository.impl.ModifierLocalRepositoryImpl
import org.turter.patrocl.data.local.repository.impl.ModifiersGroupLocalRepositoryImpl
import org.turter.patrocl.data.local.repository.impl.ModifiersSchemeLocalRepositoryImpl
import org.turter.patrocl.data.local.repository.impl.OrderItemVoidLocalRepositoryImpl
import org.turter.patrocl.data.local.repository.impl.prefs.EmployeeRepositoryImpl
import org.turter.patrocl.data.local.repository.impl.prefs.SourceDataPrefsImpl
import org.turter.patrocl.data.local.repository.impl.prefs.WaiterRepositoryImpl
import org.turter.patrocl.data.remote.client.DataVersionApiClient
import org.turter.patrocl.data.remote.client.EmployeeApiClient
import org.turter.patrocl.data.remote.client.HallApiClient
import org.turter.patrocl.data.remote.client.MenuApiClient
import org.turter.patrocl.data.remote.client.OrderApiClient
import org.turter.patrocl.data.remote.client.OrderItemVoidApiClient
import org.turter.patrocl.data.remote.client.StopListApiClient
import org.turter.patrocl.data.remote.client.WaiterApiClient
import org.turter.patrocl.data.remote.client.impl.DataVersionApiClientImpl
import org.turter.patrocl.data.remote.client.impl.EmployeeApiClientImpl
import org.turter.patrocl.data.remote.client.impl.HallApiClientImpl
import org.turter.patrocl.data.remote.client.impl.MenuApiClientImpl
import org.turter.patrocl.data.remote.client.impl.OrderApiClientImpl
import org.turter.patrocl.data.remote.client.impl.OrderItemVoidApiClientImpl
import org.turter.patrocl.data.remote.client.impl.StopListApiClientImpl
import org.turter.patrocl.data.remote.client.impl.WaiterApiClientImpl
import org.turter.patrocl.data.remote.config.HttpClientInitializer
import org.turter.patrocl.data.service.AuthServiceImpl
import org.turter.patrocl.data.service.DataVersionServiceImpl
import org.turter.patrocl.data.service.EmployeeServiceImpl
import org.turter.patrocl.data.service.MenuServiceImpl
import org.turter.patrocl.data.service.MessageServiceImpl
import org.turter.patrocl.data.service.OrderServiceImpl
import org.turter.patrocl.data.service.StopListServiceImpl
import org.turter.patrocl.data.service.WaiterServiceImpl
import org.turter.patrocl.domain.fetcher.CategoryFetcher
import org.turter.patrocl.domain.fetcher.DishFetcher
import org.turter.patrocl.domain.fetcher.HallFetcher
import org.turter.patrocl.domain.fetcher.ModifiersFetcher
import org.turter.patrocl.domain.fetcher.ModifiersGroupFetcher
import org.turter.patrocl.domain.fetcher.ModifiersSchemeFetcher
import org.turter.patrocl.domain.fetcher.OrderItemVoidFetcher
import org.turter.patrocl.domain.service.AuthService
import org.turter.patrocl.domain.service.DataVersionService
import org.turter.patrocl.domain.service.EmployeeService
import org.turter.patrocl.domain.service.MenuService
import org.turter.patrocl.domain.service.MessageService
import org.turter.patrocl.domain.service.OrderService
import org.turter.patrocl.domain.service.StopListService
import org.turter.patrocl.domain.service.WaiterService

@OptIn(ExperimentalOpenIdConnect::class)
val dataModule = module {
    //prefs
    single { Settings() }

    //remote
    single<HttpClient> {
        val initializer: HttpClientInitializer = get()
        initializer.defaultHttpClient
    }

    //auth
    single<OpenIdConnectClient> {
        val initializer: OidcClientInitializer = get()
        initializer.client
    }

    //services
    single<AuthService> {
        AuthServiceImpl(
            appAuth = get(),
            httpClient = get(),
            tokenStore = get(),
            waiterRepository = get(),
            sourceDataPrefs = get(),
            employeeRepository = get(),
            employeeService = get(),
            waiterService = get()
        )
    }

//    single<WebSocketFlowFactory> {
//        WebSocketFlowFactory(httpClient = get(), authService = get())
//    }

    single<MessageService> { MessageServiceImpl() }

    single<MenuService> {
        MenuServiceImpl(
            categoryFetcher = get(),
            modifiersFetcher = get(),
            modifiersGroupFetcher = get(),
            dishesFetcher = get(),
            orderItemVoidFetcher = get(),
            modifiersSchemeFetcher = get(),
            stopListService = get()
        )
    }

    single<StopListService> {
        StopListServiceImpl(
            stopListApiClient = get(),
            dishFetcher = get(),
            messageService = get()
        )
    }

//    single<TableService> {
//        TableServiceImpl(
//            hallApiClient = get(),
//            tableLocalSource = get(named("tableLocalSource"))
//        )
//    }

    single<WaiterService> {
        WaiterServiceImpl(waiterApiClient = get(), waiterRepository = get())
    }

    single<EmployeeService> {
        EmployeeServiceImpl(
            employeeApiClient = get(),
            employeeRepository = get()
        )
    }

    single<OrderService> { OrderServiceImpl(orderApiClient = get(), messageService = get()) }

    single<DataVersionService> {
        DataVersionServiceImpl(
            dataVersionApiClient = get(),
            employeeRepository = get(),
            dataVersionRepository = get(),
            categoryFetcher = get(),
            dishFetcher = get(),
            modifiersFetcher = get(),
            modifiersGroupFetcher = get(),
            modifiersSchemeFetcher = get(),
            hallFetcher = get(),
            orderItemVoidFetcher = get()
        )
    }

    //clients
    single<MenuApiClient> { MenuApiClientImpl(httpClient = get()) }

    single<HallApiClient> { HallApiClientImpl(httpClient = get()) }

    single<OrderApiClient> { OrderApiClientImpl(httpClient = get()) }

    single<WaiterApiClient> { WaiterApiClientImpl(httpClient = get()) }

    single<EmployeeApiClient> { EmployeeApiClientImpl(httpClient = get()) }

    single<StopListApiClient> { StopListApiClientImpl(httpClient = get()) }

    single<OrderItemVoidApiClient> { OrderItemVoidApiClientImpl(httpClient = get()) }

    single<DataVersionApiClient> { DataVersionApiClientImpl(httpClient = get()) }

    //repositories
    single<CompanySourceDataVersionLocalRepository> { CompanySourceDataVersionLocalRepositoryImpl() }
//    single<CompanySourcesInfoLocalRepository> { CompanySourcesInfoLocalRepositoryImpl() }
    single<CategoryLocalRepository> { CategoryLocalRepositoryImpl() }
    single<DishLocalRepository> { DishLocalRepositoryImpl() }
    single<ModifierLocalRepository> { ModifierLocalRepositoryImpl() }
    single<ModifiersGroupLocalRepository> { ModifiersGroupLocalRepositoryImpl() }
    single<ModifiersSchemeLocalRepository> { ModifiersSchemeLocalRepositoryImpl() }
    single<OrderItemVoidLocalRepository> { OrderItemVoidLocalRepositoryImpl() }
//    single<OwnWaiterLocalRepository> { OwnWaiterLocalRepositoryImpl() }
//    single<EmployeeLocalRepository> { EmployeeLocalRepositoryImpl() }
    single<HallLocalRepository> { HallLocalRepositoryImpl() }

    //prefs repos
    single { SourceDataPrefsImpl(settings = get()) }
    single { EmployeeRepositoryImpl(settings = get()) }
    single { WaiterRepositoryImpl(settings = get()) }

    //fetchers
    single<CategoryFetcher> {
        CategoryFetcherImpl(
            menuApiClient = get(),
            categoryRepository = get(),
            sourceDataPrefs = get(),
            versionRepository = get()
        )
    }

    single<DishFetcher> {
        DishFetcherImpl(
            menuApiClient = get(),
            dishRepository = get(),
            dataVersionRepository = get()
        )
    }

    single<ModifiersFetcher> {
        ModifiersFetcherImpl(
            menuApiClient = get(),
            modifiersRepository = get(),
            dataVersionRepository = get()
        )
    }

    single<ModifiersGroupFetcher> {
        ModifiersGroupFetcherImpl(
            menuApiClient = get(),
            modifiersGroupRepository = get(),
            sourceDataPrefs = get(),
            dataVersionRepository = get()
        )
    }

    single<ModifiersSchemeFetcher> {
        ModifiersSchemeFetcherImpl(
            menuApiClient = get(),
            modifiersSchemesRepository = get(),
            dataVersionRepository = get()
        )
    }

    single<OrderItemVoidFetcher> {
        OrderItemVoidFetcherImpl(
            voidsClient = get(),
            voidsRepository = get(),
            dataVersionRepository = get()
        )
    }

    single<HallFetcher> {
        HallFetcherImpl(
            hallApiClient = get(),
            hallRepository = get(),
            sourceDataPrefs = get(),
            dataVersionRepository = get()
        )
    }

}