package com.vb.kanjimap_android.feature.session.domain.repository

import com.vb.kanjimap_android.feature.session.domain.model.Session
import com.vb.kanjimap_android.feature.session.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val accessToken: Flow<String?>
    val isAuthenticated: Flow<Boolean>

    suspend fun login(login: String, password: String): Session
    suspend fun register(username: String, email: String, password: String): User
    suspend fun getCurrentUser(): User
    suspend fun logout()
    suspend fun getSavedAccessToken(): String?
}
