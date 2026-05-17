package com.vb.kanjimap_android.feature.library.domain.model

data class Kanji(
    val kanjiId: Long,
    val literal: String,
    val strokeCount: Int?,
    val jlptLevel: String?
)
