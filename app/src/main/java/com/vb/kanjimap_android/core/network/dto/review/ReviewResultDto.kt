package com.vb.kanjimap_android.core.network.dto.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResultDto(
    val itemId: Long,
    val itemType: String,
    val isCorrect: Boolean,
    val acceptedAnswers: List<String>,
    val status: String,
    val correctNumber: Int,
    val wrongNumber: Int,
    val repetitionLevel: Int,
    val lastReviewAt: String? = null,
    val nextReviewAt: String? = null
)
