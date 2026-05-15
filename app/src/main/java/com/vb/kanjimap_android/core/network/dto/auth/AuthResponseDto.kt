package com.vb.kanjimap_android.core.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val user: UserProfileResponseDto
)
