package com.vb.kanjimap_android.core.network.dto.progress

import kotlinx.serialization.Serializable

@Serializable
data class UpdateKanjiProgressRequestDto(
    val status: String? = null,
    val correctNumber: Int? = null,
    val wrongNumber: Int? = null,
    val repetitionLevel: Int? = null,
    val lastReviewAt: String? = null,
    val nextReviewAt: String? = null
)
