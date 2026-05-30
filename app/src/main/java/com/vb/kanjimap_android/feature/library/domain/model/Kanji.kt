package com.vb.kanjimap_android.feature.library.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Kanji(
    val kanjiId: Long,
    val literal: String,
    val strokeCount: Int?,
    val jlptLevel: String?
)
