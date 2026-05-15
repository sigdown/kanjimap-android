package com.vb.kanjimap_android.core.network.dto.review

import com.vb.kanjimap_android.core.network.dto.dictionary.KanjiSearchItemDto
import com.vb.kanjimap_android.core.network.dto.dictionary.WordSearchItemDto
import kotlinx.serialization.Serializable

@Serializable
data class ReviewItemDto(
    val itemId: Long,
    val itemType: String,
    val word: WordSearchItemDto? = null,
    val kanji: KanjiSearchItemDto? = null,
    val status: String,
    val correctNumber: Int,
    val wrongNumber: Int,
    val repetitionLevel: Int,
    val nextReviewAt: String? = null
)
