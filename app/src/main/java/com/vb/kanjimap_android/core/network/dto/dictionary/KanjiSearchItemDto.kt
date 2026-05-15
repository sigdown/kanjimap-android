package com.vb.kanjimap_android.core.network.dto.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class KanjiSearchItemDto(
    val kanjiId: Long,
    val literal: String,
    val strokeCount: Int? = null,
    val jlptLevel: String? = null
)
