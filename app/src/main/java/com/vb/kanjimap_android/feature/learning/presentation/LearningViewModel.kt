package com.vb.kanjimap_android.feature.learning.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlockDetails
import com.vb.kanjimap_android.feature.learning.domain.model.StudyCard
import com.vb.kanjimap_android.feature.learning.domain.model.StudyCardType
import com.vb.kanjimap_android.feature.learning.domain.usecase.GetBlockDetailsUseCase
import com.vb.kanjimap_android.feature.learning.domain.usecase.GetBlocksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val getBlocksUseCase: GetBlocksUseCase,
    private val getBlockDetailsUseCase: GetBlockDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearningUiState())
    val uiState: StateFlow<LearningUiState> = _uiState.asStateFlow()

    fun loadBlocks(force: Boolean = false) {
        val currentState = _uiState.value.learn
        if (!force && currentState.hasLoaded && currentState.errorMessage == null) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    learn = it.learn.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                )
            }

            runCatching { getBlocksUseCase() }
                .onSuccess { blocks ->
                    _uiState.update {
                        it.copy(
                            learn = it.learn.copy(
                                items = blocks,
                                isLoading = false,
                                errorMessage = null,
                                hasLoaded = true
                            )
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            learn = it.learn.copy(
                                items = emptyList(),
                                isLoading = false,
                                errorMessage = throwable.message ?: "Не удалось загрузить блоки",
                                hasLoaded = true
                            )
                        )
                    }
                }
        }
    }

    fun loadBlockDetails(blockId: Long, force: Boolean = false) {
        val currentState = _uiState.value.blockDetails
        if (!force &&
            currentState.blockId == blockId &&
            currentState.item != null &&
            currentState.errorMessage == null
        ) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    blockDetails = it.blockDetails.copy(
                        blockId = blockId,
                        item = null,
                        isLoading = true,
                        errorMessage = null
                    )
                )
            }

            runCatching { getBlockDetailsUseCase(blockId) }
                .onSuccess { details ->
                    _uiState.update {
                        it.copy(
                            blockDetails = it.blockDetails.copy(
                                blockId = blockId,
                                item = details,
                                isLoading = false,
                                errorMessage = null
                            )
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            blockDetails = it.blockDetails.copy(
                                blockId = blockId,
                                item = null,
                                isLoading = false,
                                errorMessage = throwable.message ?: "Не удалось загрузить блок"
                            )
                        )
                    }
                }
        }
    }

    fun startStudy(blockId: Long, mode: StudyMode) {
        viewModelScope.launch {
            val cachedDetails = _uiState.value.blockDetails.item
                ?.takeIf { it.block.learningBlockId == blockId }
            if (cachedDetails != null) {
                setStudyState(blockId = blockId, mode = mode, details = cachedDetails)
                return@launch
            }

            _uiState.update {
                it.copy(
                    study = it.study.copy(
                        blockId = blockId,
                        mode = mode,
                        cards = emptyList(),
                        currentIndex = 0,
                        isAnswerRevealed = false,
                        isLoading = true,
                        errorMessage = null
                    )
                )
            }

            runCatching { getBlockDetailsUseCase(blockId) }
                .onSuccess { details ->
                    _uiState.update {
                        it.copy(
                            blockDetails = it.blockDetails.copy(
                                blockId = blockId,
                                item = details,
                                isLoading = false,
                                errorMessage = null
                            )
                        )
                    }
                    setStudyState(blockId = blockId, mode = mode, details = details)
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            study = it.study.copy(
                                blockId = blockId,
                                mode = mode,
                                cards = emptyList(),
                                currentIndex = 0,
                                isAnswerRevealed = false,
                                isLoading = false,
                                errorMessage = throwable.message ?: "Не удалось подготовить карточки"
                            )
                        )
                    }
                }
        }
    }

    fun showAnswer() {
        _uiState.update {
            it.copy(
                study = it.study.copy(isAnswerRevealed = true)
            )
        }
    }

    fun nextCard() {
        _uiState.update { state ->
            val nextIndex = (state.study.currentIndex + 1).coerceAtMost(state.study.cards.lastIndex)
            state.copy(
                study = state.study.copy(
                    currentIndex = nextIndex,
                    isAnswerRevealed = false
                )
            )
        }
    }

    fun previousCard() {
        _uiState.update { state ->
            val previousIndex = (state.study.currentIndex - 1).coerceAtLeast(0)
            state.copy(
                study = state.study.copy(
                    currentIndex = previousIndex,
                    isAnswerRevealed = false
                )
            )
        }
    }

    fun resetStudy() {
        _uiState.update { it.copy(study = StudyUiState()) }
    }

    private fun setStudyState(
        blockId: Long,
        mode: StudyMode,
        details: LearningBlockDetails
    ) {
        val cards = buildStudyCards(details, mode)
        _uiState.update {
            it.copy(
                study = StudyUiState(
                    blockId = blockId,
                    mode = mode,
                    cards = cards,
                    currentIndex = 0,
                    isAnswerRevealed = false,
                    isLoading = false,
                    errorMessage = if (cards.isEmpty()) "В этом блоке пока нет материалов для изучения" else null
                )
            )
        }
    }

    private fun buildStudyCards(
        details: LearningBlockDetails,
        mode: StudyMode
    ): List<StudyCard> {
        val wordCards = details.words.map { word ->
            StudyCard(
                id = "word_${word.wordId}",
                type = StudyCardType.WORD,
                prompt = word.writingForm,
                answerTitle = word.writingForm,
                answerSubtitle = word.readingKana,
                readings = listOf(word.readingKana),
                meanings = listOfNotNull(word.topicName),
                examples = listOfNotNull(
                    word.jlptLevel?.let { "Уровень: $it" }
                ),
                relatedKanjis = word.writingForm
                    .filter { it.code > 0x3000 }
                    .map(Char::toString)
            )
        }

        val kanjiCards = details.kanjis.map { kanji ->
            StudyCard(
                id = "kanji_${kanji.kanjiId}",
                type = StudyCardType.KANJI,
                prompt = kanji.literal,
                answerTitle = kanji.literal,
                meanings = listOfNotNull(kanji.jlptLevel?.let { "Уровень: $it" }),
                examples = listOfNotNull(
                    kanji.strokeCount?.let { "$it черт" }
                ),
                relatedWords = details.words
                    .filter { it.writingForm.contains(kanji.literal) }
                    .map { "${it.writingForm} • ${it.readingKana}" }
            )
        }

        return when (mode) {
            StudyMode.WORDS -> wordCards
            StudyMode.KANJI -> kanjiCards
            StudyMode.ALL -> wordCards + kanjiCards
        }
    }
}
