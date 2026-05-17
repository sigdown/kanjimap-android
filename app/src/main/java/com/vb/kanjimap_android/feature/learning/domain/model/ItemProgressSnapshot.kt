package com.vb.kanjimap_android.feature.learning.domain.model

data class ItemProgressSnapshot(
    val status: String,
    val correctNumber: Int,
    val wrongNumber: Int,
    val repetitionLevel: Int,
    val nextReviewAt: String?
)
