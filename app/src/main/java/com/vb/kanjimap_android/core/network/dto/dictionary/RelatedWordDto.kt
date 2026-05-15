package com.vb.kanjimap_android.core.network.dto.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class RelatedWordDto(
    val relationType: String,
    val note: String? = null,
    val word: WordSearchItemDto? = null
)
