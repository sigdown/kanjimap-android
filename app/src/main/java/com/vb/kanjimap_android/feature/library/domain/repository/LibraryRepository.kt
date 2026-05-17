package com.vb.kanjimap_android.feature.library.domain.repository

import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.KanjiDetails
import com.vb.kanjimap_android.feature.library.domain.model.Word
import com.vb.kanjimap_android.feature.library.domain.model.WordDetails

interface LibraryRepository {
    suspend fun searchWords(query: String): List<Word>

    suspend fun searchKanji(query: String): List<Kanji>

    suspend fun getWordDetails(id: Long): WordDetails

    suspend fun getKanjiDetails(id: Long): KanjiDetails
}
