package org.turter.patrocl.presentation.auth

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.turter.patrocl.domain.model.AuthState
import org.turter.patrocl.domain.service.AuthService

sealed class AuthUiEvent {
    data object Login : AuthUiEvent()
    data class Logout(val navigate: () -> Unit) : AuthUiEvent()
}

class AuthViewModel(
    private val authService: AuthService,
    private val postLoginNav: () -> Unit
) : ScreenModel {
    private val _screenState = MutableStateFlow<WelcomeScreenState>(WelcomeScreenState.Initial)

    val screenState: StateFlow<WelcomeScreenState> = _screenState.asStateFlow()

    init {
        screenModelScope.launch {
            authService.getAuthStateFlow()
                .collect { authState ->
                    when (authState) {
                        is AuthState.Authorized -> postLoginNav()
                        is AuthState.Forbidden -> _screenState.value = WelcomeScreenState.Forbidden(
                            authState.user,
                            authState.cause
                        )

                        is AuthState.NoBindEmployee -> _screenState.value = WelcomeScreenState.NoBindEmployee(
                            authState.user,
                            authState.cause
                        )

                        is AuthState.NoBindWaiter -> _screenState.value = WelcomeScreenState.NoBindWaiter(
                            authState.user,
                            authState.employee,
                            authState.cause
                        )

                        is AuthState.NotAuthorized -> _screenState.value = WelcomeScreenState.NotAuthorized(cause = authState.cause)
                        else -> _screenState.value = WelcomeScreenState.Loading
                    }
                }
        }
    }

    fun sendEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.Login -> login()
            is AuthUiEvent.Logout -> logout(event.navigate)
        }
    }

    private fun login() {
        screenModelScope.launch {
            authService.authenticate()
                .onSuccess { postLoginNav() }
        }
    }

    private fun logout(navigate: () -> Unit) {
        screenModelScope.launch {
            authService.logout()
                .onSuccess { navigate() }
        }
    }

}