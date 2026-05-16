package com.vb.kanjimap_android.feature.session.data.mapper

import com.vb.kanjimap_android.core.network.dto.auth.AuthResponseDto
import com.vb.kanjimap_android.core.network.dto.auth.UserProfileResponseDto
import com.vb.kanjimap_android.feature.session.domain.model.Session
import com.vb.kanjimap_android.feature.session.domain.model.User

fun UserProfileResponseDto.toDomain(): User = User(
    userId = userId,
    username = username,
    email = email,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun AuthResponseDto.toDomain(): Session = Session(
    accessToken = accessToken,
    user = user.toDomain()
)
