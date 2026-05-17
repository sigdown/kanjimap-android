package com.vb.kanjimap_android.feature.learning.domain.usecase

import com.vb.kanjimap_android.feature.learning.domain.repository.LearningRepository
import javax.inject.Inject

class UpdateWordProgressUseCase @Inject constructor(
    private val repository: LearningRepository
) {
    suspend operator fun invoke(
        id: Long,
        status: String,
        correctNumber: Int,
        wrongNumber: Int,
        repetitionLevel: Int,
        lastReviewAt: String,
        nextReviewAt: String
    ) {
        repository.updateWordProgress(
            id = id,
            status = status,
            correctNumber = correctNumber,
            wrongNumber = wrongNumber,
            repetitionLevel = repetitionLevel,
            lastReviewAt = lastReviewAt,
            nextReviewAt = nextReviewAt
        )
    }
}
