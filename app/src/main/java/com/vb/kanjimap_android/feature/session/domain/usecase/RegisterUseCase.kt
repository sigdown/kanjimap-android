package com.vb.kanjimap_android.feature.session.domain.usecase

import com.vb.kanjimap_android.feature.session.domain.model.User
import com.vb.kanjimap_android.feature.session.domain.repository.SessionRepository

class RegisterUseCase(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String): User =
        sessionRepository.register(username = username, email = email, password = password)
}
