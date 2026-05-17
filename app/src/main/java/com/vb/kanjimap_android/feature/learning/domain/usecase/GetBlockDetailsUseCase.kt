package com.vb.kanjimap_android.feature.learning.domain.usecase

import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlockDetails
import com.vb.kanjimap_android.feature.learning.domain.repository.LearningRepository
import javax.inject.Inject

class GetBlockDetailsUseCase @Inject constructor(
    private val repository: LearningRepository
) {
    suspend operator fun invoke(id: Long): LearningBlockDetails = repository.getBlockDetails(id)
}
