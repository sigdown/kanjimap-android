package com.vb.kanjimap_android.feature.library.domain.model

data class Word(
    val wordId: Long,
    val writingForm: String,
    val readingKana: String,
    val jlptLevel: String?,
    val topicName: String?
)
