package com.vb.kanjimap_android.app.di

import com.vb.kanjimap_android.core.datastore.SessionDataStore
import com.vb.kanjimap_android.core.network.api.AuthApi
import com.vb.kanjimap_android.feature.session.data.repository.SessionRepositoryImpl
import com.vb.kanjimap_android.feature.session.domain.repository.SessionRepository

object RepositoryModule {
    fun provideSessionRepository(
        authApi: AuthApi,
        sessionDataStore: SessionDataStore
    ): SessionRepository = SessionRepositoryImpl(
        authApi = authApi,
        sessionDataStore = sessionDataStore
    )
}
