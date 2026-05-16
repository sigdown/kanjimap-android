package com.vb.kanjimap_android.feature.session.presentation

import com.vb.kanjimap_android.feature.session.domain.model.User

data class SessionUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val errorMessage: String? = null
)
