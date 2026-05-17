package com.vb.kanjimap_android.feature.review.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.kanjimap_android.feature.review.domain.model.ReviewItemType
import com.vb.kanjimap_android.feature.review.domain.usecase.GetReviewKanjiUseCase
import com.vb.kanjimap_android.feature.review.domain.usecase.GetReviewWordsUseCase
import com.vb.kanjimap_android.feature.review.domain.usecase.SubmitKanjiAnswerUseCase
import com.vb.kanjimap_android.feature.review.domain.usecase.SubmitWordAnswerUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.HasSavedSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val hasSavedSessionUseCase: HasSavedSessionUseCase,
    private val getReviewWordsUseCase: GetReviewWordsUseCase,
    private val getReviewKanjiUseCase: GetReviewKanjiUseCase,
    private val submitWordAnswerUseCase: SubmitWordAnswerUseCase,
    private val submitKanjiAnswerUseCase: SubmitKanjiAnswerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    init {
        loadReview()
    }

    fun loadReview() {
        viewModelScope.launch {
            val isAuthenticated = hasSavedSessionUseCase()
            if (!isAuthenticated) {
                _uiState.value = ReviewUiState(isGuest = true)
                return@launch
            }

            _uiState.update {
                it.copy(
                    isGuest = false,
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                coroutineScope {
                    val words = async { getReviewWordsUseCase() }
                    val kanji = async { getReviewKanjiUseCase() }
                    words.await() + kanji.await()
                }
            }
                .onSuccess { queue ->
                    _uiState.value = ReviewUiState(
                        isGuest = false,
                        isLoading = false,
                        queue = queue,
                        currentIndex = 0,
                        isAnswerRevealed = false,
                        answerInput = "",
                        result = null,
                        isCompleted = false
                    )
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Не удалось загрузить повторение"
                        )
                    }
                }
        }
    }

    fun revealAnswer() {
        _uiState.update { it.copy(isAnswerRevealed = true) }
    }

    fun updateAnswerInput(value: String) {
        _uiState.update { it.copy(answerInput = value) }
    }

    fun submitAnswer() {
        val state = _uiState.value
        val current = state.currentItem ?: return
        if (state.answerInput.isBlank() || state.isSubmitting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            runCatching {
                when (current.itemType) {
                    ReviewItemType.WORD -> submitWordAnswerUseCase(current.itemId, state.answerInput.trim())
                    ReviewItemType.KANJI -> submitKanjiAnswerUseCase(current.itemId, state.answerInput.trim())
                }
            }
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            result = result
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = throwable.message ?: "Не удалось проверить ответ"
                        )
                    }
                }
        }
    }

    fun nextCard() {
        _uiState.update { state ->
            val nextIndex = state.currentIndex + 1
            if (nextIndex > state.queue.lastIndex) {
                state.copy(
                    isCompleted = true,
                    result = null,
                    answerInput = "",
                    isAnswerRevealed = false
                )
            } else {
                state.copy(
                    currentIndex = nextIndex,
                    result = null,
                    answerInput = "",
                    isAnswerRevealed = false
                )
            }
        }
    }
}
