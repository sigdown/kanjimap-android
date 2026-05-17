package com.vb.kanjimap_android.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.kanjimap_android.feature.home.domain.usecase.GetHomeSummaryUseCase
import com.vb.kanjimap_android.feature.home.domain.usecase.HasSavedHomeSessionUseCase
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
    private val hasSavedHomeSessionUseCase: HasSavedHomeSessionUseCase,
    private val getHomeSummaryUseCase: GetHomeSummaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            val isAuthenticated = hasSavedHomeSessionUseCase()
            if (!isAuthenticated) {
                _uiState.value = HomeUiState(isGuest = true)
                return@launch
            }

            _uiState.update {
                it.copy(
                    isLoading = true,
                    isGuest = false,
                    errorMessage = null
                )
            }

            runCatching { getHomeSummaryUseCase() }
                .onSuccess { summary ->
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        isGuest = false,
                        summary = summary,
                        errorMessage = null
                    )
                }
                .onFailure { throwable ->
                    if (throwable is HttpException && throwable.code() in listOf(401, 403)) {
                        _uiState.value = HomeUiState(isGuest = true)
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isGuest = false,
                                summary = null,
                                errorMessage = throwable.message ?: "Не удалось загрузить Home"
                            )
                        }
                    }
                }
        }
    }
}
