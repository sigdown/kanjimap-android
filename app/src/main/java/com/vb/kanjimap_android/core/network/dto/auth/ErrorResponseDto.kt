package com.vb.kanjimap_android.core.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(
    val error: String
)
