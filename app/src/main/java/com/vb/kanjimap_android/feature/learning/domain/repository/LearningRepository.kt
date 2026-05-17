package com.vb.kanjimap_android.feature.learning.domain.repository

import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlock
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlockDetails

interface LearningRepository {
    suspend fun getBlocks(): List<LearningBlock>

    suspend fun getBlockDetails(id: Long): LearningBlockDetails
}
