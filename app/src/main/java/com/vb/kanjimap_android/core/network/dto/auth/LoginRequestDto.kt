package com.vb.kanjimap_android.core.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val login: String,
    val password: String
)
