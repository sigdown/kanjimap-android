package com.vb.kanjimap_android.feature.review.presentation

import com.vb.kanjimap_android.feature.review.domain.model.ReviewItem
import com.vb.kanjimap_android.feature.review.domain.model.ReviewResult

data class ReviewUiState(
    val isGuest: Boolean = true,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null,
    val queue: List<ReviewItem> = emptyList(),
    val currentIndex: Int = 0,
    val isAnswerRevealed: Boolean = false,
    val answerInput: String = "",
    val result: ReviewResult? = null
) {
    val currentItem: ReviewItem?
        get() = queue.getOrNull(currentIndex)

    val progressText: String
        get() = if (queue.isEmpty()) "0 / 0" else "${currentIndex + 1} / ${queue.size}"

    val isEmpty: Boolean
        get() = !isLoading && !isGuest && !isCompleted && queue.isEmpty() && errorMessage == null
}
