package com.vb.kanjimap_android.app.di

import com.vb.kanjimap_android.feature.home.data.repository.HomeRepositoryImpl
import com.vb.kanjimap_android.feature.home.domain.repository.HomeRepository
import com.vb.kanjimap_android.feature.library.data.repository.LibraryRepositoryImpl
import com.vb.kanjimap_android.feature.library.domain.repository.LibraryRepository
import com.vb.kanjimap_android.feature.learning.data.repository.LearningRepositoryImpl
import com.vb.kanjimap_android.feature.learning.domain.repository.LearningRepository
import com.vb.kanjimap_android.feature.review.data.repository.ReviewRepositoryImpl
import com.vb.kanjimap_android.feature.review.domain.repository.ReviewRepository
import com.vb.kanjimap_android.feature.session.data.repository.SessionRepositoryImpl
import com.vb.kanjimap_android.feature.session.domain.repository.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindLibraryRepository(
        libraryRepositoryImpl: LibraryRepositoryImpl
    ): LibraryRepository

    @Binds
    @Singleton
    abstract fun bindLearningRepository(
        learningRepositoryImpl: LearningRepositoryImpl
    ): LearningRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository
}
