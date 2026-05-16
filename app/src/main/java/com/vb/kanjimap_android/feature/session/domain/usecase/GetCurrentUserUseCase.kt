package com.vb.kanjimap_android.feature.session.domain.usecase

import com.vb.kanjimap_android.feature.session.domain.model.User
import com.vb.kanjimap_android.feature.session.domain.repository.SessionRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): User = sessionRepository.getCurrentUser()
}
