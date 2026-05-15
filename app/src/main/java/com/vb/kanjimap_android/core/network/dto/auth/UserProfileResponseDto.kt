package com.vb.kanjimap_android.core.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponseDto(
    val userId: Long,
    val username: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String
)
