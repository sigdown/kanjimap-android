package com.vb.kanjimap_android.core.network.dto.progress

import kotlinx.serialization.Serializable

@Serializable
data class KanjiProgressDto(
    val kanjiId: Long,
    val status: String,
    val correctNumber: Int,
    val wrongNumber: Int,
    val repetitionLevel: Int,
    val lastReviewAt: String? = null,
    val nextReviewAt: String? = null,
    val updatedAt: String
)
