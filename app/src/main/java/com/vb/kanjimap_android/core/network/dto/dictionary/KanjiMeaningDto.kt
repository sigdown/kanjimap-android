package com.vb.kanjimap_android.core.network.dto.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class KanjiMeaningDto(
    val kanjiMeaningId: Long,
    val languageCode: String,
    val meaning: String,
    val example: String? = null
)
