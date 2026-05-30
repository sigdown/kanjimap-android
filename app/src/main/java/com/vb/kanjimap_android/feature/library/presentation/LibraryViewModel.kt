package com.vb.kanjimap_android.feature.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.kanjimap_android.feature.library.domain.usecase.GetKanjiDetailsUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.GetSavedKanjiUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.GetSavedWordsUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.GetWordDetailsUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.IsKanjiSavedUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.IsWordSavedUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.SaveKanjiUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.SaveWordUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.SearchKanjiUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.SearchWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private val getKanjiDetailsUseCase: GetKanjiDetailsUseCase,
    private val saveWordUseCase: SaveWordUseCase,
    private val saveKanjiUseCase: SaveKanjiUseCase,
    private val getSavedWordsUseCase: GetSavedWordsUseCase,
    private val getSavedKanjiUseCase: GetSavedKanjiUseCase,
    private val isWordSavedUseCase: IsWordSavedUseCase,
    private val isKanjiSavedUseCase: IsKanjiSavedUseCase
) : ViewModel() {

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
        const val SEARCH_SUGGESTIONS_LIMIT = 10
    }

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events: SharedFlow<String> = _events.asSharedFlow()

    private var wordSearchJob: Job? = null
    private var kanjiSearchJob: Job? = null
    private var wordSavedStatusJob: Job? = null
    private var kanjiSavedStatusJob: Job? = null

    init {
        viewModelScope.launch {
            getSavedWordsUseCase().collect { words ->
                _uiState.update { it.copy(savedWords = SavedWordsUiState(items = words)) }
            }
        }

        viewModelScope.launch {
            getSavedKanjiUseCase().collect { kanji ->
                _uiState.update { it.copy(savedKanji = SavedKanjiUiState(items = kanji)) }
            }
        }
    }

    fun updateWordsQuery(query: String) {
        _uiState.update {
            it.copy(
                wordSearch = it.wordSearch.copy(
                    query = query,
                    errorMessage = null
                )
            )
        }

        scheduleWordsSearch(query)
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

        scheduleKanjiSearch(query)
    }

    fun searchWords(query: String = _uiState.value.wordSearch.query) {
        wordSearchJob?.cancel()
        wordSearchJob = viewModelScope.launch {
            searchWordsInternal(query)
        }
    }

    fun searchKanji(query: String = _uiState.value.kanjiSearch.query) {
        kanjiSearchJob?.cancel()
        kanjiSearchJob = viewModelScope.launch {
            searchKanjiInternal(query)
        }
    }

    private fun scheduleWordsSearch(query: String) {
        wordSearchJob?.cancel()
        if (query.isBlank()) {
            clearWordSearch(query)
            return
        }

        wordSearchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            searchWordsInternal(query)
        }
    }

    private fun scheduleKanjiSearch(query: String) {
        kanjiSearchJob?.cancel()
        if (query.isBlank()) {
            clearKanjiSearch(query)
            return
        }

        kanjiSearchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            searchKanjiInternal(query)
        }
    }

    private suspend fun searchWordsInternal(query: String) {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            clearWordSearch(query)
            return
        }

        _uiState.update {
            it.copy(
                wordSearch = it.wordSearch.copy(
                    query = normalizedQuery,
                    isLoading = true,
                    errorMessage = null
                )
            )
        }

        try {
            val words = searchWordsUseCase(normalizedQuery).take(SEARCH_SUGGESTIONS_LIMIT)
            _uiState.update {
                it.copy(
                    wordSearch = it.wordSearch.copy(
                        items = words,
                        isLoading = false,
                        errorMessage = null
                    )
                )
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (throwable: Throwable) {
            _uiState.update {
                it.copy(
                    wordSearch = it.wordSearch.copy(
                        items = emptyList(),
                        isLoading = false,
                        errorMessage = throwable.message ?: "Не удалось загрузить слова"
                    )
                )
            }
        }
    }

    private suspend fun searchKanjiInternal(query: String) {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            clearKanjiSearch(query)
            return
        }

        _uiState.update {
            it.copy(
                kanjiSearch = it.kanjiSearch.copy(
                    query = normalizedQuery,
                    isLoading = true,
                    errorMessage = null
                )
            )
        }

        try {
            val kanji = searchKanjiUseCase(normalizedQuery).take(SEARCH_SUGGESTIONS_LIMIT)
            _uiState.update {
                it.copy(
                    kanjiSearch = it.kanjiSearch.copy(
                        items = kanji,
                        isLoading = false,
                        errorMessage = null
                    )
                )
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (throwable: Throwable) {
            _uiState.update {
                it.copy(
                    kanjiSearch = it.kanjiSearch.copy(
                        items = emptyList(),
                        isLoading = false,
                        errorMessage = throwable.message ?: "Не удалось загрузить кандзи"
                    )
                )
            }
        }
    }

    private fun clearWordSearch(query: String) {
        _uiState.update {
            it.copy(
                wordSearch = it.wordSearch.copy(
                    query = query,
                    items = emptyList(),
                    isLoading = false,
                    errorMessage = null
                )
            )
        }
    }

    private fun clearKanjiSearch(query: String) {
        _uiState.update {
            it.copy(
                kanjiSearch = it.kanjiSearch.copy(
                    query = query,
                    items = emptyList(),
                    isLoading = false,
                    errorMessage = null
                )
            )
        }
    }

    fun loadWordDetails(id: Long) {
        observeWordSavedStatus(id)
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
        observeKanjiSavedStatus(id)
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
        val details = _uiState.value.wordDetails.item ?: return
        if (_uiState.value.wordDetails.isSaved) {
            _events.tryEmit("Слово уже сохранено")
            return
        }

        viewModelScope.launch {
            runCatching { saveWordUseCase(details) }
                .onSuccess { _events.emit("Слово сохранено") }
                .onFailure { throwable ->
                    _events.emit(throwable.message ?: "Не удалось сохранить слово")
                }
        }
    }

    fun onSaveKanjiClick() {
        val details = _uiState.value.kanjiDetails.item ?: return
        if (_uiState.value.kanjiDetails.isSaved) {
            _events.tryEmit("Кандзи уже сохранено")
            return
        }

        viewModelScope.launch {
            runCatching { saveKanjiUseCase(details) }
                .onSuccess { _events.emit("Кандзи сохранено") }
                .onFailure { throwable ->
                    _events.emit(throwable.message ?: "Не удалось сохранить кандзи")
                }
        }
    }

    private fun observeWordSavedStatus(wordId: Long) {
        wordSavedStatusJob?.cancel()
        wordSavedStatusJob = viewModelScope.launch {
            isWordSavedUseCase(wordId).collect { isSaved ->
                _uiState.update {
                    it.copy(
                        wordDetails = it.wordDetails.copy(isSaved = isSaved)
                    )
                }
            }
        }
    }

    private fun observeKanjiSavedStatus(kanjiId: Long) {
        kanjiSavedStatusJob?.cancel()
        kanjiSavedStatusJob = viewModelScope.launch {
            isKanjiSavedUseCase(kanjiId).collect { isSaved ->
                _uiState.update {
                    it.copy(
                        kanjiDetails = it.kanjiDetails.copy(isSaved = isSaved)
                    )
                }
            }
        }
    }
}
