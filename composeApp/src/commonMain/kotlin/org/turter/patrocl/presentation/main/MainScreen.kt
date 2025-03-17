package org.turter.patrocl.presentation.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import co.touchlab.kermit.Logger
import org.koin.core.parameter.parametersOf
import org.turter.patrocl.presentation.auth.WelcomeScreen
import org.turter.patrocl.presentation.auth.redirectToAuth
import org.turter.patrocl.presentation.components.CircularLoader
import org.turter.patrocl.presentation.components.FullscreenLoader
import org.turter.patrocl.presentation.error.ErrorComponent
import org.turter.patrocl.presentation.error.ErrorType
import org.turter.patrocl.presentation.main.components.MainErrorScreen
import org.turter.patrocl.presentation.main.components.MainScreenLoader
import org.turter.patrocl.presentation.main.components.SnackbarMessageHost
import org.turter.patrocl.presentation.orders.list.OrdersScreen
import org.turter.patrocl.presentation.stoplist.list.StopListScreen

class MainScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val vm: MainViewModel = koinScreenModel{ parametersOf({ navigator.redirectToAuth() }) }
        val screenState by vm.mainScreenState.collectAsState()

        AnimatedContent(
            targetState = screenState,
            transitionSpec = {
                fadeIn(initialAlpha = 0.4f) togetherWith
                        fadeOut(targetAlpha = 0.4f) using SizeTransform(clip = false)
            }
        ) { state ->
            when (state) {
                is MainScreenState.Content -> {
                    TabNavigator(tab = OrdersTab) {
                        Scaffold {
                            CurrentTab()
                            SnackbarMessageHost(
                                messageState = state.messageState.collectAsState()
                            )
                        }
                    }
                }

                is MainScreenState.CheckingData -> MainScreenLoader(supportingText = "Проверка актуальности версий данных")

                is MainScreenState.UpdatingData -> MainScreenLoader(supportingText = "Обновление данных")

                is MainScreenState.ActualizeDataError -> ErrorComponent(
                    error = ErrorType.from(state.cause),
                    onRetry = { vm.sendEvent(MainUiEvent.ActualizeData) }
                )

//                is MainScreenState.Error -> MainErrorScreen(
//                    errorType = state.errorType,
////                    onRetry = { vm.sendEvent(MainUiEvent.RefreshWaiter) }
//                    onRetry = {  }
//                )

                else -> MainScreenLoader(supportingText = "Загрузка")
            }
        }
    }


}