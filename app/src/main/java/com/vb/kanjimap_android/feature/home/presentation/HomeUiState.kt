package com.vb.kanjimap_android.feature.home.presentation

import com.vb.kanjimap_android.feature.home.domain.model.HomeSummary

data class HomeUiState(
    val isLoading: Boolean = false,
    val isGuest: Boolean = true,
    val summary: HomeSummary? = null,
    val errorMessage: String? = null
)
