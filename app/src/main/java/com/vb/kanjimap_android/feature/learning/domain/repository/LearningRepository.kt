package com.vb.kanjimap_android.feature.learning.domain.repository

import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlock
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlockDetails
import com.vb.kanjimap_android.feature.learning.domain.model.ItemProgressSnapshot

interface LearningRepository {
    suspend fun getBlocks(): List<LearningBlock>

    suspend fun getBlockDetails(id: Long): LearningBlockDetails

    suspend fun getWordProgress(id: Long): ItemProgressSnapshot

    suspend fun getKanjiProgress(id: Long): ItemProgressSnapshot

    suspend fun updateWordProgress(
        id: Long,
        status: String,
        correctNumber: Int,
        wrongNumber: Int,
        repetitionLevel: Int,
        lastReviewAt: String,
        nextReviewAt: String
    )

    suspend fun updateKanjiProgress(
        id: Long,
        status: String,
        correctNumber: Int,
        wrongNumber: Int,
        repetitionLevel: Int,
        lastReviewAt: String,
        nextReviewAt: String
    )
}
