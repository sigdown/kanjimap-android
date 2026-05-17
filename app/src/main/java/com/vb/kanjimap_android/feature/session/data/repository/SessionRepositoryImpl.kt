package com.vb.kanjimap_android.feature.session.data.repository

import com.vb.kanjimap_android.core.datastore.SessionDataStore
import com.vb.kanjimap_android.core.network.api.AuthApi
import com.vb.kanjimap_android.core.network.dto.auth.LoginRequestDto
import com.vb.kanjimap_android.core.network.dto.auth.RegisterRequestDto
import com.vb.kanjimap_android.feature.session.data.mapper.toDomain
import com.vb.kanjimap_android.feature.session.domain.model.Session
import com.vb.kanjimap_android.feature.session.domain.model.User
import com.vb.kanjimap_android.feature.session.domain.repository.SessionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

class SessionRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionDataStore: SessionDataStore
) : SessionRepository {

    override suspend fun login(login: String, password: String): Session {
        val response = authApi.loginUser(
            LoginRequestDto(
                login = login,
                password = password
            )
        )

        sessionDataStore.setAccessToken(response.accessToken)
        sessionDataStore.setCurrentUserId(response.user.userId)

        return response.toDomain()
    }

    override suspend fun register(username: String, email: String, password: String): User {
        val response = authApi.registerUser(
            RegisterRequestDto(
                username = username,
                email = email,
                password = password
            )
        )

        return response.user.toDomain()
    }

    override suspend fun getCurrentUser(): User {
        val response = authApi.getCurrentUser()
        sessionDataStore.setCurrentUserId(response.userId)
        return response.toDomain()
    }

    override suspend fun logout() {
        sessionDataStore.clearSession()
    }

    override suspend fun hasSavedSession(): Boolean =
        !getSavedAccessToken().isNullOrBlank()

    override suspend fun getSavedAccessToken(): String? =
        sessionDataStore.accessToken.firstOrNull()
}
