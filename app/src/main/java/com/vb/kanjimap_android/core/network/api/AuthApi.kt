package com.vb.kanjimap_android.core.network.api

import com.vb.kanjimap_android.core.network.dto.auth.AuthResponseDto
import com.vb.kanjimap_android.core.network.dto.auth.HealthResponseDto
import com.vb.kanjimap_android.core.network.dto.auth.LoginRequestDto
import com.vb.kanjimap_android.core.network.dto.auth.RegisterRequestDto
import com.vb.kanjimap_android.core.network.dto.auth.UserProfileResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @GET("/health")
    suspend fun healthCheck(): HealthResponseDto

    @POST("auth/register")
    suspend fun registerUser(@Body body: RegisterRequestDto): UserProfileResponseDto

    @POST("auth/login")
    suspend fun loginUser(@Body body: LoginRequestDto): AuthResponseDto

    @GET("auth/me")
    suspend fun getCurrentUser(): UserProfileResponseDto
}
