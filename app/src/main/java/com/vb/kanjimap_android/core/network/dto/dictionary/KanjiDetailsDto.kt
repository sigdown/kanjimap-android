package com.vb.kanjimap_android.core.network.dto.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class KanjiDetailsDto(
    val kanji: KanjiSearchItemDto,
    val onReadings: List<String>,
    val kunReadings: List<String>,
    val nanoriReadings: List<String>,
    val meanings: List<KanjiMeaningDto>,
    val words: List<WordSearchItemDto>
)
