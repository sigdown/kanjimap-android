package com.vb.kanjimap_android.feature.review.domain.model

data class ReviewResult(
    val itemId: Long,
    val itemType: ReviewItemType,
    val isCorrect: Boolean,
    val acceptedAnswers: List<String>,
    val status: String,
    val correctNumber: Int,
    val wrongNumber: Int,
    val repetitionLevel: Int,
    val lastReviewAt: String? = null,
    val nextReviewAt: String? = null
)
