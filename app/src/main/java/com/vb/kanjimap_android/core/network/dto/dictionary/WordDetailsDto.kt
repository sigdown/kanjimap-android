package com.vb.kanjimap_android.core.network.dto.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class WordDetailsDto(
    val word: WordSearchItemDto,
    val meanings: List<WordMeaningDto>,
    val relatedWords: List<RelatedWordDto>,
    val kanjis: List<KanjiSearchItemDto>
)
