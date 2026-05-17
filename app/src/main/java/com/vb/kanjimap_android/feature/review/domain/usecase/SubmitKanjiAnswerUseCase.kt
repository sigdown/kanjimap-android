package com.vb.kanjimap_android.feature.review.domain.usecase

import com.vb.kanjimap_android.feature.review.domain.model.ReviewResult
import com.vb.kanjimap_android.feature.review.domain.repository.ReviewRepository
import javax.inject.Inject

class SubmitKanjiAnswerUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(id: Long, answer: String): ReviewResult =
        reviewRepository.submitKanjiAnswer(id, answer)
}
