package com.vb.kanjimap_android.feature.library.domain.model

data class WordDetails(
    val word: Word,
    val meanings: List<WordMeaning>,
    val relatedWords: List<RelatedWord>,
    val kanjis: List<Kanji>
)
