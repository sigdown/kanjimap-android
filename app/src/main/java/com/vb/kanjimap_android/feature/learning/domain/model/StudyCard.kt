package com.vb.kanjimap_android.feature.learning.domain.model

data class StudyCard(
    val id: String,
    val itemId: Long,
    val type: StudyCardType,
    val prompt: String,
    val answerTitle: String? = null,
    val answerSubtitle: String? = null,
    val meanings: List<String> = emptyList(),
    val readings: List<String> = emptyList(),
    val onReadings: List<String> = emptyList(),
    val kunReadings: List<String> = emptyList(),
    val nanoriReadings: List<String> = emptyList(),
    val examples: List<String> = emptyList(),
    val relatedKanjis: List<String> = emptyList(),
    val relatedWords: List<String> = emptyList()
)

enum class StudyCardType {
    WORD,
    KANJI
}
