package com.vb.kanjimap_android.feature.session.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.kanjimap_android.feature.session.domain.repository.SessionRepository
import com.vb.kanjimap_android.feature.session.domain.usecase.GetCurrentUserUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.LoginUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.LogoutUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionViewModel(
    private val sessionRepository: SessionRepository,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        observeSessionState()
        checkSavedSession()
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            runCatchingWithLoading {
                val session = loginUseCase(login, password)
                _uiState.update {
                    it.copy(
                        isAuthenticated = true,
                        currentUser = session.user,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            runCatchingWithLoading {
                val user = registerUseCase(username, email, password)
                _uiState.update {
                    it.copy(
                        currentUser = user,
                        errorMessage = null
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

    fun checkSavedSession() {
        viewModelScope.launch {
            val hasToken = !sessionRepository.getSavedAccessToken().isNullOrBlank()
            _uiState.update {
                it.copy(
                    isAuthenticated = hasToken,
                    errorMessage = null
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun observeSessionState() {
        viewModelScope.launch {
            sessionRepository.isAuthenticated.collect { isAuthenticated ->
                _uiState.update { state ->
                    state.copy(
                        isAuthenticated = isAuthenticated,
                        currentUser = if (isAuthenticated) state.currentUser else null
                    )
                }
            }
        }
    }

    private suspend fun runCatchingWithLoading(action: suspend () -> Unit) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        runCatching { action() }
            .onFailure { throwable ->
                _uiState.update {
                    it.copy(errorMessage = throwable.message ?: "Unknown error")
                }
            }
        _uiState.update { it.copy(isLoading = false) }
    }
}
