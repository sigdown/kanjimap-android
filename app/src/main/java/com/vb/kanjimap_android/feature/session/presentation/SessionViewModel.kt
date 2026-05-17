package com.vb.kanjimap_android.feature.session.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.kanjimap_android.feature.session.domain.usecase.HasSavedSessionUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.GetCurrentUserUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.LoginUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.LogoutUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val hasSavedSessionUseCase: HasSavedSessionUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        checkSession()
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            runCatchingWithLoading {
                val session = loginUseCase(login, password)
                _uiState.update {
                    it.copy(
                        isAuthenticated = true,
                        currentUser = session.user,
                        errorMessage = null,
                        isRegisterMode = false
                    )
                }
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            runCatchingWithLoading {
                registerUseCase(username, email, password)
                _uiState.update {
                    it.copy(
                        isAuthenticated = false,
                        currentUser = null,
                        errorMessage = null,
                        isRegisterMode = false
                    )
                }
            }
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            runCatchingWithLoading {
                val user = getCurrentUserUseCase()
                _uiState.update {
                    it.copy(
                        isAuthenticated = true,
                        currentUser = user,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runCatchingWithLoading {
                logoutUseCase()
                _uiState.update {
                    it.copy(
                        isAuthenticated = false,
                        currentUser = null,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                if (!hasSavedSessionUseCase()) {
                    _uiState.update {
                        it.copy(
                            isAuthenticated = false,
                            currentUser = null,
                            errorMessage = null
                        )
                    }
                    return@runCatching
                }

                val currentUser = getCurrentUserUseCase()
                _uiState.update {
                    it.copy(
                        isAuthenticated = true,
                        currentUser = currentUser,
                        errorMessage = null
                    )
                }
            }
                .onFailure { throwable ->
                    if (throwable is HttpException && throwable.code() in listOf(401, 403)) {
                        logoutUseCase()
                        _uiState.update {
                            it.copy(
                                isAuthenticated = false,
                                currentUser = null,
                                errorMessage = throwable.message ?: "Unknown error"
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isAuthenticated = true,
                                errorMessage = throwable.message ?: "Unknown error"
                            )
                        }
                    }
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun setRegisterMode(isRegisterMode: Boolean) {
        _uiState.update {
            it.copy(
                isRegisterMode = isRegisterMode,
                errorMessage = null
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private suspend fun runCatchingWithLoading(action: suspend () -> Unit) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        runCatching { action() }
            .onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isAuthenticated = false,
                        currentUser = null,
                        errorMessage = throwable.message ?: "Unknown error"
                    )
                }
            }
        _uiState.update { it.copy(isLoading = false) }
    }
}
