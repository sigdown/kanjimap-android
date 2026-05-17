package com.vb.kanjimap_android.feature.library.data.repository

import com.vb.kanjimap_android.core.network.api.DictionaryApi
import com.vb.kanjimap_android.feature.library.data.mapper.toDomain
import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.KanjiDetails
import com.vb.kanjimap_android.feature.library.domain.model.Word
import com.vb.kanjimap_android.feature.library.domain.model.WordDetails
import com.vb.kanjimap_android.feature.library.domain.repository.LibraryRepository
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi
) : LibraryRepository {

    override suspend fun searchWords(query: String): List<Word> =
        dictionaryApi.searchWords(query).map { it.toDomain() }

    override suspend fun searchKanji(query: String): List<Kanji> =
        dictionaryApi.searchKanji(query).map { it.toDomain() }

    override suspend fun getWordDetails(id: Long): WordDetails =
        dictionaryApi.getWordDetails(id).toDomain()

    override suspend fun getKanjiDetails(id: Long): KanjiDetails =
        dictionaryApi.getKanjiDetails(id).toDomain()
}
