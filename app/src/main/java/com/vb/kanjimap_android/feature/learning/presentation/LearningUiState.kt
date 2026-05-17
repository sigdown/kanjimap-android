package com.vb.kanjimap_android.feature.learning.presentation

import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlock
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlockDetails
import com.vb.kanjimap_android.feature.learning.domain.model.StudyCard

data class LearningUiState(
    val learn: LearnUiState = LearnUiState(),
    val blockDetails: BlockDetailsUiState = BlockDetailsUiState(),
    val study: StudyUiState = StudyUiState()
)

data class LearnUiState(
    val items: List<LearningBlock> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasLoaded: Boolean = false
)

data class BlockDetailsUiState(
    val blockId: Long? = null,
    val item: LearningBlockDetails? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class StudyUiState(
    val blockId: Long? = null,
    val mode: StudyMode? = null,
    val cards: List<StudyCard> = emptyList(),
    val currentIndex: Int = 0,
    val isAnswerRevealed: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val currentCard: StudyCard?
        get() = cards.getOrNull(currentIndex)

    val progressText: String
        get() = if (cards.isEmpty()) "0 / 0" else "${currentIndex + 1} / ${cards.size}"
}

enum class StudyMode(val value: String) {
    WORDS("words"),
    KANJI("kanji"),
    ALL("all");

    companion object {
        fun fromValue(value: String?): StudyMode = entries.firstOrNull { it.value == value } ?: WORDS
    }
}
