package com.vb.kanjimap_android.feature.library.domain.model

data class KanjiDetails(
    val kanji: Kanji,
    val onReadings: List<String>,
    val kunReadings: List<String>,
    val nanoriReadings: List<String>,
    val meanings: List<KanjiMeaning>,
    val words: List<Word>
)
