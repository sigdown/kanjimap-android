package com.vb.kanjimap_android.feature.library.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RelatedWord(
    val relationType: String,
    val note: String?,
    val word: Word?
)
