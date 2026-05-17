package com.vb.kanjimap_android.feature.review.data.repository

import com.vb.kanjimap_android.core.network.api.ReviewApi
import com.vb.kanjimap_android.core.network.dto.review.CheckKanjiAnswerRequestDto
import com.vb.kanjimap_android.core.network.dto.review.CheckWordAnswerRequestDto
import com.vb.kanjimap_android.feature.review.data.mapper.toDomain
import com.vb.kanjimap_android.feature.review.domain.model.ReviewItem
import com.vb.kanjimap_android.feature.review.domain.model.ReviewResult
import com.vb.kanjimap_android.feature.review.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi
) : ReviewRepository {

    override suspend fun getReviewWords(): List<ReviewItem> =
        reviewApi.getReviewWords().map { it.toDomain() }

    override suspend fun getReviewKanji(): List<ReviewItem> =
        reviewApi.getReviewKanji().map { it.toDomain() }

    override suspend fun submitWordAnswer(id: Long, answer: String): ReviewResult =
        reviewApi.checkWordAnswer(id, CheckWordAnswerRequestDto(answer)).toDomain()

    override suspend fun submitKanjiAnswer(id: Long, answer: String): ReviewResult =
        reviewApi.checkKanjiAnswer(id, CheckKanjiAnswerRequestDto(answer)).toDomain()
}
