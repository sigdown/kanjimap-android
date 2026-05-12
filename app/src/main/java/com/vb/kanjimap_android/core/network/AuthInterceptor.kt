package com.vb.kanjimap_android.core.network

import com.vb.kanjimap_android.core.datastore.SessionDataStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionDataStore: SessionDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = sessionDataStore.getAccessTokenSync()

        val request = if (token.isNullOrBlank()) {
            original
        } else {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        return chain.proceed(request)
    }
}