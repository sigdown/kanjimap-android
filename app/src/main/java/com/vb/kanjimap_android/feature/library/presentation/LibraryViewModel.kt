package com.vb.kanjimap_android.feature.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.kanjimap_android.feature.library.domain.usecase.GetKanjiDetailsUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.GetWordDetailsUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.SearchKanjiUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.SearchWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val searchWordsUseCase: SearchWordsUseCase,
    private val searchKanjiUseCase: SearchKanjiUseCase,
    private val getWordDetailsUseCase: GetWordDetailsUseCase,
    private val getKanjiDetailsUseCase: GetKanjiDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events: SharedFlow<String> = _events.asSharedFlow()

    fun updateWordsQuery(query: String) {
        _uiState.update {
            it.copy(
                wordSearch = it.wordSearch.copy(
                    query = query,
                    errorMessage = null
                )
            )
        }
    }

    fun updateKanjiQuery(query: String) {
        _uiState.update {
            it.copy(
                kanjiSearch = it.kanjiSearch.copy(
                    query = query,
                    errorMessage = null
                )
            )
        }
    }

    fun searchWords(query: String = _uiState.value.wordSearch.query) {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            _uiState.update {
                it.copy(
                    wordSearch = it.wordSearch.copy(
                        query = query,
                        items = emptyList(),
                        isLoading = false,
                        errorMessage = null,
                        hasSearched = false
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    wordSearch = it.wordSearch.copy(
                        query = normalizedQuery,
                        isLoading = true,
                        errorMessage = null,
                        hasSearched = true
                    )
                )
            }

            runCatching { searchWordsUseCase(normalizedQuery) }
                .onSuccess { words ->
                    _uiState.update {
                        it.copy(
                            wordSearch = it.wordSearch.copy(
                                items = words,
                                isLoading = false,
                                errorMessage = null,
                                hasSearched = true
                            )
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            wordSearch = it.wordSearch.copy(
                                items = emptyList(),
                                isLoading = false,
                                errorMessage = throwable.message ?: "Не удалось загрузить слова",
                                hasSearched = true
                            )
                        )
                    }
                }
        }
    }

    fun searchKanji(query: String = _uiState.value.kanjiSearch.query) {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            _uiState.update {
                it.copy(
                    kanjiSearch = it.kanjiSearch.copy(
                        query = query,
                        items = emptyList(),
                        isLoading = false,
                        errorMessage = null,
                        hasSearched = false
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    kanjiSearch = it.kanjiSearch.copy(
                        query = normalizedQuery,
                        isLoading = true,
                        errorMessage = null,
                        hasSearched = true
                    )
                )
            }

            runCatching { searchKanjiUseCase(normalizedQuery) }
                .onSuccess { kanji ->
                    _uiState.update {
                        it.copy(
                            kanjiSearch = it.kanjiSearch.copy(
                                items = kanji,
                                isLoading = false,
                                errorMessage = null,
                                hasSearched = true
                            )
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            kanjiSearch = it.kanjiSearch.copy(
                                items = emptyList(),
                                isLoading = false,
                                errorMessage = throwable.message ?: "Не удалось загрузить кандзи",
                                hasSearched = true
                            )
                        )
                    }
                }
        }
    }

    fun loadWordDetails(id: Long) {
        if (_uiState.value.wordDetails.wordId == id &&
            _uiState.value.wordDetails.item != null &&
            _uiState.value.wordDetails.errorMessage == null
        ) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    wordDetails = it.wordDetails.copy(
                        wordId = id,
                        item = null,
                        isLoading = true,
                        errorMessage = null
                    )
                )
            }

            runCatching { getWordDetailsUseCase(id) }
                .onSuccess { details ->
                    _uiState.update {
                        it.copy(
                            wordDetails = it.wordDetails.copy(
                                wordId = id,
                                item = details,
                                isLoading = false,
                                errorMessage = null
                            )
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            wordDetails = it.wordDetails.copy(
                                wordId = id,
                                item = null,
                                isLoading = false,
                                errorMessage = throwable.message
                                    ?: "Не удалось загрузить детали слова"
                            )
                        )
                    }
                }
        }
    }

    fun loadKanjiDetails(id: Long) {
        if (_uiState.value.kanjiDetails.kanjiId == id &&
            _uiState.value.kanjiDetails.item != null &&
            _uiState.value.kanjiDetails.errorMessage == null
        ) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    kanjiDetails = it.kanjiDetails.copy(
                        kanjiId = id,
                        item = null,
                        isLoading = true,
                        errorMessage = null
                    )
                )
            }

            runCatching { getKanjiDetailsUseCase(id) }
                .onSuccess { details ->
                    _uiState.update {
                        it.copy(
                            kanjiDetails = it.kanjiDetails.copy(
                                kanjiId = id,
                                item = details,
                                isLoading = false,
                                errorMessage = null
                            )
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            kanjiDetails = it.kanjiDetails.copy(
                                kanjiId = id,
                                item = null,
                                isLoading = false,
                                errorMessage = throwable.message
                                    ?: "Не удалось загрузить детали кандзи"
                            )
                        )
                    }
                }
        }
    }

    fun onSaveWordClick() {
        _events.tryEmit("Сохранение слова будет добавлено позже")
    }

    fun onSaveKanjiClick() {
        _events.tryEmit("Сохранение кандзи будет добавлено позже")
    }
}
