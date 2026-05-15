package com.vb.kanjimap_android.core.network.dto.review

import kotlinx.serialization.Serializable

@Serializable
data class CheckKanjiAnswerRequestDto(
    val answer: String
)
