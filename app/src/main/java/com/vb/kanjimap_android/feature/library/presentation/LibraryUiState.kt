package com.vb.kanjimap_android.feature.library.presentation

import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.KanjiDetails
import com.vb.kanjimap_android.feature.library.domain.model.Word
import com.vb.kanjimap_android.feature.library.domain.model.WordDetails

data class LibraryUiState(
    val wordSearch: WordSearchUiState = WordSearchUiState(),
    val kanjiSearch: KanjiSearchUiState = KanjiSearchUiState(),
    val wordDetails: WordDetailsUiState = WordDetailsUiState(),
    val kanjiDetails: KanjiDetailsUiState = KanjiDetailsUiState(),
    val savedWords: SavedWordsUiState = SavedWordsUiState(),
    val savedKanji: SavedKanjiUiState = SavedKanjiUiState()
)

data class WordSearchUiState(
    val query: String = "",
    val items: List<Word> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class KanjiSearchUiState(
    val query: String = "",
    val items: List<Kanji> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class WordDetailsUiState(
    val wordId: Long? = null,
    val item: WordDetails? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)

data class KanjiDetailsUiState(
    val kanjiId: Long? = null,
    val item: KanjiDetails? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)

data class SavedWordsUiState(
    val items: List<Word> = emptyList()
)

data class SavedKanjiUiState(
    val items: List<Kanji> = emptyList()
)
