package com.vb.kanjimap_android.feature.home.domain.model

data class HomeSummary(
    val username: String,
    val reviewWordsCount: Int,
    val reviewKanjiCount: Int,
    val totalReviewCount: Int,
    val blocksPreview: List<HomeBlockPreview>
)

data class HomeBlockPreview(
    val blockId: Long,
    val title: String,
    val blockType: String
)
