package com.vb.kanjimap_android.feature.review.domain.model

data class ReviewItem(
    val itemId: Long,
    val itemType: ReviewItemType,
    val word: ReviewWord? = null,
    val kanji: ReviewKanji? = null,
    val status: String,
    val correctNumber: Int,
    val wrongNumber: Int,
    val repetitionLevel: Int,
    val nextReviewAt: String? = null
)

data class ReviewWord(
    val wordId: Long,
    val writingForm: String,
    val readingKana: String,
    val jlptLevel: String? = null,
    val topicName: String? = null
)

data class ReviewKanji(
    val kanjiId: Long,
    val literal: String,
    val strokeCount: Int? = null,
    val jlptLevel: String? = null
)

enum class ReviewItemType {
    WORD,
    KANJI
}
