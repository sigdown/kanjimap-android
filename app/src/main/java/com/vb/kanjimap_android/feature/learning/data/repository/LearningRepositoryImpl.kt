package com.vb.kanjimap_android.feature.learning.data.repository

import com.vb.kanjimap_android.core.network.api.LearningApi
import com.vb.kanjimap_android.feature.learning.data.mapper.toDomain
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlock
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlockDetails
import com.vb.kanjimap_android.feature.learning.domain.repository.LearningRepository
import javax.inject.Inject

class LearningRepositoryImpl @Inject constructor(
    private val learningApi: LearningApi
) : LearningRepository {

    override suspend fun getBlocks(): List<LearningBlock> =
        learningApi.getLearningBlocks().map { it.toDomain() }

    override suspend fun getBlockDetails(id: Long): LearningBlockDetails =
        learningApi.getLearningBlockDetails(id).toDomain()
}
