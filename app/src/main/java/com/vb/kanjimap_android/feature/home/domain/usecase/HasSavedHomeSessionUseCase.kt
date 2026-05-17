package com.vb.kanjimap_android.feature.home.domain.usecase

import com.vb.kanjimap_android.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class HasSavedHomeSessionUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Boolean = homeRepository.hasSavedSession()
}
