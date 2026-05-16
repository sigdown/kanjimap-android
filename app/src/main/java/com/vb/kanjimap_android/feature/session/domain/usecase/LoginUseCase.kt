package com.vb.kanjimap_android.feature.session.domain.usecase

import com.vb.kanjimap_android.feature.session.domain.model.Session
import com.vb.kanjimap_android.feature.session.domain.repository.SessionRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(login: String, password: String): Session =
        sessionRepository.login(login = login, password = password)
}
