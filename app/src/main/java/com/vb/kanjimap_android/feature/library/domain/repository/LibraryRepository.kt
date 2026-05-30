package com.vb.kanjimap_android.feature.library.domain.repository

import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.KanjiDetails
import com.vb.kanjimap_android.feature.library.domain.model.Word
import com.vb.kanjimap_android.feature.library.domain.model.WordDetails
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun searchWords(query: String): List<Word>

    suspend fun searchKanji(query: String): List<Kanji>

    suspend fun getWordDetails(id: Long): WordDetails

    suspend fun getKanjiDetails(id: Long): KanjiDetails

    suspend fun saveWord(details: WordDetails)

    suspend fun saveKanji(details: KanjiDetails)

    fun getSavedWords(): Flow<List<Word>>

    fun getSavedKanji(): Flow<List<Kanji>>

    fun isWordSaved(wordId: Long): Flow<Boolean>

    fun isKanjiSaved(kanjiId: Long): Flow<Boolean>
}
