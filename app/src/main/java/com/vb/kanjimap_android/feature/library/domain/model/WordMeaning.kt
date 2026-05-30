package com.vb.kanjimap_android.feature.library.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WordMeaning(
    val meaningId: Long,
    val meaning: String,
    val exampleJp: String?,
    val exampleTranslation: String?,
    val partOfSpeech: String?
)
