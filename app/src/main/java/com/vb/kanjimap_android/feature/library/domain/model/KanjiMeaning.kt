package com.vb.kanjimap_android.feature.library.domain.model

data class KanjiMeaning(
    val kanjiMeaningId: Long,
    val languageCode: String,
    val meaning: String,
    val example: String?
)
