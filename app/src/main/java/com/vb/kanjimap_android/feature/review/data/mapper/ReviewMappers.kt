package com.vb.kanjimap_android.feature.review.data.mapper

import com.vb.kanjimap_android.core.network.dto.review.ReviewItemDto
import com.vb.kanjimap_android.core.network.dto.review.ReviewResultDto
import com.vb.kanjimap_android.feature.review.domain.model.ReviewItem
import com.vb.kanjimap_android.feature.review.domain.model.ReviewItemType
import com.vb.kanjimap_android.feature.review.domain.model.ReviewKanji
import com.vb.kanjimap_android.feature.review.domain.model.ReviewResult
import com.vb.kanjimap_android.feature.review.domain.model.ReviewWord

fun ReviewItemDto.toDomain(): ReviewItem = ReviewItem(
    itemId = itemId,
    itemType = itemType.toReviewType(),
    word = word?.let {
        ReviewWord(
            wordId = it.wordId,
            writingForm = it.writingForm,
            readingKana = it.readingKana,
            jlptLevel = it.jlptLevel,
            topicName = it.topicName
        )
    },
    kanji = kanji?.let {
        ReviewKanji(
            kanjiId = it.kanjiId,
            literal = it.literal,
            strokeCount = it.strokeCount,
            jlptLevel = it.jlptLevel
        )
    },
    status = status,
    correctNumber = correctNumber,
    wrongNumber = wrongNumber,
    repetitionLevel = repetitionLevel,
    nextReviewAt = nextReviewAt
)

fun ReviewResultDto.toDomain(): ReviewResult = ReviewResult(
    itemId = itemId,
    itemType = itemType.toReviewType(),
    isCorrect = isCorrect,
    acceptedAnswers = acceptedAnswers,
    status = status,
    correctNumber = correctNumber,
    wrongNumber = wrongNumber,
    repetitionLevel = repetitionLevel,
    lastReviewAt = lastReviewAt,
    nextReviewAt = nextReviewAt
)

private fun String.toReviewType(): ReviewItemType =
    if (equals("kanji", ignoreCase = true)) ReviewItemType.KANJI else ReviewItemType.WORD
