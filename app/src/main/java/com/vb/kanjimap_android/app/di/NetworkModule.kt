package com.vb.kanjimap_android.app.di

import com.vb.kanjimap_android.core.network.AuthInterceptor
import com.vb.kanjimap_android.core.network.NetworkFactory
import com.vb.kanjimap_android.core.network.api.AuthApi
import com.vb.kanjimap_android.core.network.api.DictionaryApi
import com.vb.kanjimap_android.core.network.api.LearningApi
import com.vb.kanjimap_android.core.network.api.ProgressApi
import com.vb.kanjimap_android.core.network.api.ReviewApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient = NetworkFactory.createOkHttpClient(authInterceptor)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = NetworkFactory.createRetrofit(okHttpClient)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = NetworkFactory.createAuthApi(retrofit)

    @Provides
    @Singleton
    fun provideDictionaryApi(retrofit: Retrofit): DictionaryApi =
        NetworkFactory.createDictionaryApi(retrofit)

    @Provides
    @Singleton
    fun provideLearningApi(retrofit: Retrofit): LearningApi =
        NetworkFactory.createLearningApi(retrofit)

    @Provides
    @Singleton
    fun provideProgressApi(retrofit: Retrofit): ProgressApi =
        NetworkFactory.createProgressApi(retrofit)

    @Provides
    @Singleton
    fun provideReviewApi(retrofit: Retrofit): ReviewApi = NetworkFactory.createReviewApi(retrofit)
}
