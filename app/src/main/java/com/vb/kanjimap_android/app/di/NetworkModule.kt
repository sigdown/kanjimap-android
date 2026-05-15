package com.vb.kanjimap_android.app.di

import com.vb.kanjimap_android.core.datastore.SessionDataStore
import com.vb.kanjimap_android.core.network.AuthInterceptor
import com.vb.kanjimap_android.core.network.NetworkFactory
import com.vb.kanjimap_android.core.network.api.AuthApi
import com.vb.kanjimap_android.core.network.api.DictionaryApi
import com.vb.kanjimap_android.core.network.api.LearningApi
import com.vb.kanjimap_android.core.network.api.ProgressApi
import com.vb.kanjimap_android.core.network.api.ReviewApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object NetworkModule {
    fun provideAuthInterceptor(sessionDataStore: SessionDataStore): AuthInterceptor =
        AuthInterceptor(sessionDataStore)

    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        NetworkFactory.createOkHttpClient(authInterceptor)

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        NetworkFactory.createRetrofit(okHttpClient)

    fun provideAuthApi(retrofit: Retrofit): AuthApi = NetworkFactory.createAuthApi(retrofit)
    fun provideDictionaryApi(retrofit: Retrofit): DictionaryApi = NetworkFactory.createDictionaryApi(retrofit)
    fun provideLearningApi(retrofit: Retrofit): LearningApi = NetworkFactory.createLearningApi(retrofit)
    fun provideProgressApi(retrofit: Retrofit): ProgressApi = NetworkFactory.createProgressApi(retrofit)
    fun provideReviewApi(retrofit: Retrofit): ReviewApi = NetworkFactory.createReviewApi(retrofit)
}
