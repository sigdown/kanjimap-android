package com.vb.kanjimap_android.core.network
import com.vb.kanjimap_android.core.common.Constants
import com.vb.kanjimap_android.core.network.api.AuthApi
import com.vb.kanjimap_android.core.network.api.DictionaryApi
import com.vb.kanjimap_android.core.network.api.LearningApi
import com.vb.kanjimap_android.core.network.api.ProgressApi
import com.vb.kanjimap_android.core.network.api.ReviewApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object NetworkFactory {
    val json: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    fun createOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(Constants.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    fun createRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(Constants.CONTENT_TYPE_JSON.toMediaType()))
            .build()

    fun createAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
    fun createDictionaryApi(retrofit: Retrofit): DictionaryApi = retrofit.create(DictionaryApi::class.java)
    fun createLearningApi(retrofit: Retrofit): LearningApi = retrofit.create(LearningApi::class.java)
    fun createProgressApi(retrofit: Retrofit): ProgressApi = retrofit.create(ProgressApi::class.java)
    fun createReviewApi(retrofit: Retrofit): ReviewApi = retrofit.create(ReviewApi::class.java)
}
