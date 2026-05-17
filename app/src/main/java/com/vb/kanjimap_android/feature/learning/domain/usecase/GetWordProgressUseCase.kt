package com.vb.kanjimap_android.feature.learning.domain.usecase

import com.vb.kanjimap_android.feature.learning.domain.model.ItemProgressSnapshot
import com.vb.kanjimap_android.feature.learning.domain.repository.LearningRepository
import javax.inject.Inject

class GetWordProgressUseCase @Inject constructor(
    private val repository: LearningRepository
) {
    suspend operator fun invoke(id: Long): ItemProgressSnapshot = repository.getWordProgress(id)
}
