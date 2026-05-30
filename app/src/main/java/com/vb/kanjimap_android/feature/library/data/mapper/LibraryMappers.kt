package com.vb.kanjimap_android.feature.library.data.mapper

import com.vb.kanjimap_android.core.network.dto.dictionary.KanjiDetailsDto
import com.vb.kanjimap_android.core.network.dto.dictionary.KanjiMeaningDto
import com.vb.kanjimap_android.core.network.dto.dictionary.KanjiSearchItemDto
import com.vb.kanjimap_android.core.network.dto.dictionary.RelatedWordDto
import com.vb.kanjimap_android.core.network.dto.dictionary.WordDetailsDto
import com.vb.kanjimap_android.core.network.dto.dictionary.WordMeaningDto
import com.vb.kanjimap_android.core.network.dto.dictionary.WordSearchItemDto
import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.KanjiDetails
import com.vb.kanjimap_android.feature.library.domain.model.KanjiMeaning
import com.vb.kanjimap_android.feature.library.domain.model.RelatedWord
import com.vb.kanjimap_android.feature.library.domain.model.Word
import com.vb.kanjimap_android.feature.library.domain.model.WordDetails
import com.vb.kanjimap_android.feature.library.domain.model.WordMeaning
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun WordSearchItemDto.toDomain(): Word = Word(
    wordId = wordId,
    writingForm = writingForm,
    readingKana = readingKana,
    jlptLevel = jlptLevel,
    topicName = topicName
)

fun WordMeaningDto.toDomain(): WordMeaning = WordMeaning(
    meaningId = meaningId,
    meaning = meaning,
    exampleJp = exampleJp,
    exampleTranslation = exampleTranslation,
    partOfSpeech = partOfSpeech
)

fun RelatedWordDto.toDomain(): RelatedWord = RelatedWord(
    relationType = relationType,
    note = note,
    word = word?.toDomain()
)

fun KanjiSearchItemDto.toDomain(): Kanji = Kanji(
    kanjiId = kanjiId,
    literal = literal,
    strokeCount = strokeCount,
    jlptLevel = jlptLevel
)

fun KanjiMeaningDto.toDomain(): KanjiMeaning = KanjiMeaning(
    kanjiMeaningId = kanjiMeaningId,
    languageCode = languageCode,
    meaning = meaning,
    example = example
)

fun WordDetailsDto.toDomain(): WordDetails = WordDetails(
    word = word.toDomain(),
    meanings = meanings.map(WordMeaningDto::toDomain),
    relatedWords = relatedWords.map(RelatedWordDto::toDomain),
    kanjis = kanjis.map(KanjiSearchItemDto::toDomain)
)

fun KanjiDetailsDto.toDomain(): KanjiDetails = KanjiDetails(
    kanji = kanji.toDomain(),
    onReadings = onReadings,
    kunReadings = kunReadings,
    nanoriReadings = nanoriReadings,
    meanings = meanings.map(KanjiMeaningDto::toDomain),
    words = words.map(WordSearchItemDto::toDomain)
)

private val detailsJson = Json {
    ignoreUnknownKeys = true
}

fun WordDetails.toJson(): String = detailsJson.encodeToString(this)

fun String.toWordDetails(): WordDetails = detailsJson.decodeFromString(this)

fun KanjiDetails.toJson(): String = detailsJson.encodeToString(this)

fun String.toKanjiDetails(): KanjiDetails = detailsJson.decodeFromString(this)
