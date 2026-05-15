package com.vb.kanjimap_android.core.network.dto.learning

import kotlinx.serialization.Serializable

@Serializable
data class LearningBlockDto(
    val learningBlockId: Long,
    val title: String,
    val description: String? = null,
    val blockType: String,
    val orderIndex: Int
)
