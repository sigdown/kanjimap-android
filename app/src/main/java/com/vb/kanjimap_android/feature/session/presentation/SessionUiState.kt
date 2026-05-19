package com.vb.kanjimap_android.feature.session.presentation

import com.vb.kanjimap_android.feature.session.domain.model.User

enum class SessionAuthState {
    CHECKING,
    GUEST,
    AUTHENTICATED
}

data class SessionUiState(
    val isLoading: Boolean = false,
    val isCheckingSession: Boolean = true,
    val isAuthenticated: Boolean = false,
    val currentUser: User? = null,
    val errorMessage: String? = null,
    val isRegisterMode: Boolean = false
) {
    val authState: SessionAuthState
        get() = when {
            isCheckingSession -> SessionAuthState.CHECKING
            isAuthenticated && currentUser != null -> SessionAuthState.AUTHENTICATED
            else -> SessionAuthState.GUEST
        }
}
