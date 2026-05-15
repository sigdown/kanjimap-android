package com.vb.kanjimap_android.core.network.dto.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class WordSearchItemDto(
    val wordId: Long,
    val writingForm: String,
    val readingKana: String,
    val jlptLevel: String? = null,
    val topicName: String? = null
)
