package com.vb.kanjimap_android.app.di

import com.vb.kanjimap_android.feature.session.domain.repository.SessionRepository
import com.vb.kanjimap_android.feature.session.domain.usecase.GetCurrentUserUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.LoginUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.LogoutUseCase
import com.vb.kanjimap_android.feature.session.domain.usecase.RegisterUseCase

object UseCaseModule {
    fun provideLoginUseCase(sessionRepository: SessionRepository): LoginUseCase =
        LoginUseCase(sessionRepository)

    fun provideRegisterUseCase(sessionRepository: SessionRepository): RegisterUseCase =
        RegisterUseCase(sessionRepository)

    fun provideGetCurrentUserUseCase(sessionRepository: SessionRepository): GetCurrentUserUseCase =
        GetCurrentUserUseCase(sessionRepository)

    fun provideLogoutUseCase(sessionRepository: SessionRepository): LogoutUseCase =
        LogoutUseCase(sessionRepository)
}
