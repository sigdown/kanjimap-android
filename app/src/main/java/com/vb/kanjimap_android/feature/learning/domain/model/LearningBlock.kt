package com.vb.kanjimap_android.feature.learning.domain.model

data class LearningBlock(
    val learningBlockId: Long,
    val title: String,
    val description: String?,
    val blockType: String,
    val orderIndex: Int
)
