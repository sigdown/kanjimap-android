package com.vb.kanjimap_android.feature.home.domain.usecase

import com.vb.kanjimap_android.feature.home.domain.model.HomeSummary
import com.vb.kanjimap_android.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeSummaryUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): HomeSummary = homeRepository.getHomeSummary()
}
