package com.vb.kanjimap_android.feature.review.domain.usecase

import com.vb.kanjimap_android.feature.review.domain.model.ReviewItem
import com.vb.kanjimap_android.feature.review.domain.repository.ReviewRepository
import javax.inject.Inject

class GetReviewKanjiUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(): List<ReviewItem> = reviewRepository.getReviewKanji()
}
