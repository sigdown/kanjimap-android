package com.vb.kanjimap_android.feature.library.domain.model

data class RelatedWord(
    val relationType: String,
    val note: String?,
    val word: Word?
)
