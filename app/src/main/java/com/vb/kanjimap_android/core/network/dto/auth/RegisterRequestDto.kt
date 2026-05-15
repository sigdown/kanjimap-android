package com.vb.kanjimap_android.core.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val username: String,
    val email: String,
    val password: String
)
