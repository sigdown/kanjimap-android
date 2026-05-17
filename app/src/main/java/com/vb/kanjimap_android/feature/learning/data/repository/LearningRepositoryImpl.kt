package com.vb.kanjimap_android.feature.learning.data.repository

import com.vb.kanjimap_android.core.network.api.LearningApi
import com.vb.kanjimap_android.core.network.api.ProgressApi
import com.vb.kanjimap_android.core.network.dto.progress.UpdateKanjiProgressRequestDto
import com.vb.kanjimap_android.core.network.dto.progress.UpdateWordProgressRequestDto
import com.vb.kanjimap_android.feature.learning.data.mapper.toDomain
import com.vb.kanjimap_android.feature.learning.domain.model.ItemProgressSnapshot
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlock
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlockDetails
import com.vb.kanjimap_android.feature.learning.domain.repository.LearningRepository
import javax.inject.Inject

class LearningRepositoryImpl @Inject constructor(
    private val learningApi: LearningApi,
    private val progressApi: ProgressApi
) : LearningRepository {

    override suspend fun getBlocks(): List<LearningBlock> =
        learningApi.getLearningBlocks().map { it.toDomain() }

    override suspend fun getBlockDetails(id: Long): LearningBlockDetails =
        learningApi.getLearningBlockDetails(id).toDomain()

    override suspend fun getWordProgress(id: Long): ItemProgressSnapshot {
        val progress = progressApi.getWordProgress(id)
        return ItemProgressSnapshot(
            status = progress.status,
            correctNumber = progress.correctNumber,
            wrongNumber = progress.wrongNumber,
            repetitionLevel = progress.repetitionLevel,
            nextReviewAt = progress.nextReviewAt
        )
    }

    override suspend fun getKanjiProgress(id: Long): ItemProgressSnapshot {
        val progress = progressApi.getKanjiProgress(id)
        return ItemProgressSnapshot(
            status = progress.status,
            correctNumber = progress.correctNumber,
            wrongNumber = progress.wrongNumber,
            repetitionLevel = progress.repetitionLevel,
            nextReviewAt = progress.nextReviewAt
        )
    }

    override suspend fun updateWordProgress(
        id: Long,
        status: String,
        correctNumber: Int,
        wrongNumber: Int,
        repetitionLevel: Int,
        lastReviewAt: String,
        nextReviewAt: String
    ) {
        progressApi.updateWordProgress(
            id = id,
            body = UpdateWordProgressRequestDto(
                status = status,
                correctNumber = correctNumber,
                wrongNumber = wrongNumber,
                repetitionLevel = repetitionLevel,
                lastReviewAt = lastReviewAt,
                nextReviewAt = nextReviewAt
            )
        )
    }

    override suspend fun updateKanjiProgress(
        id: Long,
        status: String,
        correctNumber: Int,
        wrongNumber: Int,
        repetitionLevel: Int,
        lastReviewAt: String,
        nextReviewAt: String
    ) {
        progressApi.updateKanjiProgress(
            id = id,
            body = UpdateKanjiProgressRequestDto(
                status = status,
                correctNumber = correctNumber,
                wrongNumber = wrongNumber,
                repetitionLevel = repetitionLevel,
                lastReviewAt = lastReviewAt,
                nextReviewAt = nextReviewAt
            )
        )
    }
}
