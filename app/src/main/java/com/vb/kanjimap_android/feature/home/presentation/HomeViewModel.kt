package com.vb.kanjimap_android.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.kanjimap_android.feature.home.domain.usecase.GetHomeSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeSummaryUseCase: GetHomeSummaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var hasAttemptedAuthenticatedLoad = false

    fun loadHome(force: Boolean = false) {
        if (!force && hasAttemptedAuthenticatedLoad) return

        viewModelScope.launch {
            hasAttemptedAuthenticatedLoad = true

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching { getHomeSummaryUseCase() }
                .onSuccess { summary ->
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        summary = summary,
                        errorMessage = null
                    )
                }
                .onFailure { throwable ->
                    if (throwable is HttpException && throwable.code() in listOf(401, 403)) {
                        hasAttemptedAuthenticatedLoad = false
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            summary = null,
                            errorMessage = throwable.message ?: "Не удалось загрузить Home"
                        )
                    }
                }
        }
    }

    fun clearHome() {
        hasAttemptedAuthenticatedLoad = false
        _uiState.value = HomeUiState()
    }
}
