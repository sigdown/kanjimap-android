package com.vb.kanjimap_android.feature.review.domain.repository

import com.vb.kanjimap_android.feature.review.domain.model.ReviewItem
import com.vb.kanjimap_android.feature.review.domain.model.ReviewResult

interface ReviewRepository {
    suspend fun getReviewWords(): List<ReviewItem>
    suspend fun getReviewKanji(): List<ReviewItem>
    suspend fun submitWordAnswer(id: Long, answer: String): ReviewResult
    suspend fun submitKanjiAnswer(id: Long, answer: String): ReviewResult
}
