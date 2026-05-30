package com.vb.kanjimap_android.feature.library.data.repository

import com.vb.kanjimap_android.core.database.dao.SavedKanjiDao
import com.vb.kanjimap_android.core.database.dao.SavedWordDao
import com.vb.kanjimap_android.core.database.entity.SavedKanjiEntity
import com.vb.kanjimap_android.core.database.entity.SavedWordEntity
import com.vb.kanjimap_android.core.network.api.DictionaryApi
import com.vb.kanjimap_android.feature.library.data.mapper.toJson
import com.vb.kanjimap_android.feature.library.data.mapper.toKanjiDetails
import com.vb.kanjimap_android.feature.library.data.mapper.toDomain
import com.vb.kanjimap_android.feature.library.data.mapper.toWordDetails
import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.KanjiDetails
import com.vb.kanjimap_android.feature.library.domain.model.Word
import com.vb.kanjimap_android.feature.library.domain.model.WordDetails
import com.vb.kanjimap_android.feature.library.domain.repository.LibraryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LibraryRepositoryImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val savedWordDao: SavedWordDao,
    private val savedKanjiDao: SavedKanjiDao
) : LibraryRepository {

    override suspend fun searchWords(query: String): List<Word> =
        dictionaryApi.searchWords(query).map { it.toDomain() }

    override suspend fun searchKanji(query: String): List<Kanji> =
        dictionaryApi.searchKanji(query).map { it.toDomain() }

    override suspend fun getWordDetails(id: Long): WordDetails {
        savedWordDao.getByIdOnce(id)?.detailsJson?.let { detailsJson ->
            return detailsJson.toWordDetails()
        }

        val details = dictionaryApi.getWordDetails(id).toDomain()
        savedWordDao.getByIdOnce(id)?.let { savedWord ->
            savedWordDao.insert(savedWord.copy(detailsJson = details.toJson()))
        }
        return details
    }

    override suspend fun getKanjiDetails(id: Long): KanjiDetails {
        savedKanjiDao.getByIdOnce(id)?.detailsJson?.let { detailsJson ->
            return detailsJson.toKanjiDetails()
        }

        val details = dictionaryApi.getKanjiDetails(id).toDomain()
        savedKanjiDao.getByIdOnce(id)?.let { savedKanji ->
            savedKanjiDao.insert(savedKanji.copy(detailsJson = details.toJson()))
        }
        return details
    }

    override suspend fun saveWord(details: WordDetails) {
        val word = details.word
        savedWordDao.insert(
            SavedWordEntity(
                wordId = word.wordId,
                writingForm = word.writingForm,
                readingKana = word.readingKana,
                jlptLevel = word.jlptLevel,
                topicName = word.topicName,
                detailsJson = details.toJson(),
                savedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun saveKanji(details: KanjiDetails) {
        val kanji = details.kanji
        savedKanjiDao.insert(
            SavedKanjiEntity(
                kanjiId = kanji.kanjiId,
                literal = kanji.literal,
                strokeCount = kanji.strokeCount,
                jlptLevel = kanji.jlptLevel,
                detailsJson = details.toJson(),
                savedAt = System.currentTimeMillis()
            )
        )
    }

    override fun getSavedWords(): Flow<List<Word>> =
        savedWordDao.getAll().map { entities ->
            entities.map { entity ->
                Word(
                    wordId = entity.wordId,
                    writingForm = entity.writingForm,
                    readingKana = entity.readingKana,
                    jlptLevel = entity.jlptLevel,
                    topicName = entity.topicName
                )
            }
        }

    override fun getSavedKanji(): Flow<List<Kanji>> =
        savedKanjiDao.getAll().map { entities ->
            entities.map { entity ->
                Kanji(
                    kanjiId = entity.kanjiId,
                    literal = entity.literal,
                    strokeCount = entity.strokeCount,
                    jlptLevel = entity.jlptLevel
                )
            }
        }

    override fun isWordSaved(wordId: Long): Flow<Boolean> =
        savedWordDao.getById(wordId).map { it != null }

    override fun isKanjiSaved(kanjiId: Long): Flow<Boolean> =
        savedKanjiDao.getById(kanjiId).map { it != null }
}
