package com.vb.kanjimap_android.feature.learning.domain.usecase

import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlock
import com.vb.kanjimap_android.feature.learning.domain.repository.LearningRepository
import javax.inject.Inject

class GetBlocksUseCase @Inject constructor(
    private val repository: LearningRepository
) {
    suspend operator fun invoke(): List<LearningBlock> = repository.getBlocks()
}
