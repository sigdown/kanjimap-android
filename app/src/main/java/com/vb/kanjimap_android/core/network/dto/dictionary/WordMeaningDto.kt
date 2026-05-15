package com.vb.kanjimap_android.core.network.dto.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class WordMeaningDto(
    val meaningId: Long,
    val meaning: String,
    val exampleJp: String? = null,
    val exampleTranslation: String? = null,
    val partOfSpeech: String? = null
)
