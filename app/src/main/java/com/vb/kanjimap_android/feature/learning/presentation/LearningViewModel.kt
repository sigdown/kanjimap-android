package com.vb.kanjimap_android.feature.learning.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.kanjimap_android.core.common.time.toEpochMillisOrNull
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlockDetails
import com.vb.kanjimap_android.feature.learning.domain.model.StudyCard
import com.vb.kanjimap_android.feature.learning.domain.model.StudyCardType
import com.vb.kanjimap_android.feature.learning.domain.usecase.GetBlockDetailsUseCase
import com.vb.kanjimap_android.feature.learning.domain.usecase.GetBlocksUseCase
import com.vb.kanjimap_android.feature.learning.domain.usecase.GetKanjiProgressUseCase
import com.vb.kanjimap_android.feature.learning.domain.usecase.GetWordProgressUseCase
import com.vb.kanjimap_android.feature.learning.domain.usecase.UpdateKanjiProgressUseCase
import com.vb.kanjimap_android.feature.learning.domain.usecase.UpdateWordProgressUseCase
import com.vb.kanjimap_android.feature.library.domain.usecase.GetWordDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val getBlocksUseCase: GetBlocksUseCase,
    private val getBlockDetailsUseCase: GetBlockDetailsUseCase,
    private val getWordDetailsUseCase: GetWordDetailsUseCase,
    private val getWordProgressUseCase: GetWordProgressUseCase,
    private val getKanjiProgressUseCase: GetKanjiProgressUseCase,
    private val updateWordProgressUseCase: UpdateWordProgressUseCase,
    private val updateKanjiProgressUseCase: UpdateKanjiProgressUseCase
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
                loadAndApplyWordMeanings(blockId = blockId, mode = mode, details = cachedDetails)
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
                    loadAndApplyWordMeanings(blockId = blockId, mode = mode, details = details)
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
                    isAnswerRevealed = false,
                    submitErrorMessage = null
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
                    isAnswerRevealed = false,
                    submitErrorMessage = null
                )
            )
        }
    }

    fun markKnown() {
        submitProgress(isKnown = true)
    }

    fun markUnknown() {
        submitProgress(isKnown = false)
    }

    fun resetStudy() {
        _uiState.update { it.copy(study = StudyUiState()) }
    }

    private fun setStudyState(
        blockId: Long,
        mode: StudyMode,
        details: LearningBlockDetails,
        wordMeanings: Map<Long, List<String>> = emptyMap()
    ) {
        val cards = buildStudyCards(details, mode, wordMeanings)
        _uiState.update {
            it.copy(
                study = StudyUiState(
                    blockId = blockId,
                    mode = mode,
                    cards = cards,
                    currentIndex = 0,
                    isAnswerRevealed = false,
                    isLoading = false,
                    errorMessage = if (cards.isEmpty()) "В этом блоке пока нет материалов для изучения" else null,
                    isSubmittingProgress = false,
                    submitErrorMessage = null
                )
            )
        }
    }

    private fun buildStudyCards(
        details: LearningBlockDetails,
        mode: StudyMode,
        wordMeanings: Map<Long, List<String>> = emptyMap()
    ): List<StudyCard> {
        val wordCards = details.words.map { word ->
            StudyCard(
                id = "word_${word.wordId}",
                itemId = word.wordId,
                type = StudyCardType.WORD,
                prompt = word.writingForm,
                answerTitle = word.writingForm,
                answerSubtitle = word.readingKana,
                readings = listOf(word.readingKana),
                meanings = wordMeanings[word.wordId].orEmpty(),
                examples = emptyList(),
                relatedKanjis = emptyList()
            )
        }

        val kanjiCards = details.kanjis.map { kanji ->
            StudyCard(
                id = "kanji_${kanji.kanjiId}",
                itemId = kanji.kanjiId,
                type = StudyCardType.KANJI,
                prompt = kanji.literal,
                answerTitle = kanji.literal,
                meanings = emptyList(),
                examples = emptyList(),
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

    private fun loadAndApplyWordMeanings(
        blockId: Long,
        mode: StudyMode,
        details: LearningBlockDetails
    ) {
        if (details.words.isEmpty()) return

        viewModelScope.launch {
            val wordMeanings = fetchWordMeanings(details)
            if (wordMeanings.isEmpty()) return@launch

            val currentStudy = _uiState.value.study
            if (currentStudy.blockId != blockId || currentStudy.mode != mode) return@launch

            setStudyState(
                blockId = blockId,
                mode = mode,
                details = details,
                wordMeanings = wordMeanings
            )
        }
    }

    private suspend fun fetchWordMeanings(details: LearningBlockDetails): Map<Long, List<String>> =
        coroutineScope {
            details.words
                .map { word ->
                    async {
                        val meanings = runCatching { getWordDetailsUseCase(word.wordId) }
                            .getOrNull()
                            ?.meanings
                            ?.map { it.meaning.trim() }
                            ?.filter { it.isNotBlank() }
                            ?.distinct()
                            .orEmpty()
                        word.wordId to meanings
                    }
                }
                .awaitAll()
                .filter { it.second.isNotEmpty() }
                .toMap()
        }

    private fun submitProgress(isKnown: Boolean) {
        val currentState = _uiState.value.study
        val card = currentState.currentCard ?: return
        if (!currentState.isAnswerRevealed || currentState.isSubmittingProgress) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    study = it.study.copy(
                        isSubmittingProgress = true,
                        submitErrorMessage = null
                    )
                )
            }

            val now = Instant.now()
            val nextReviewAt = if (isKnown) {
                now.plusSeconds(24 * 60 * 60)
            } else {
                now.plusSeconds(20 * 60)
            }

            val correctNumber = if (isKnown) 1 else 0
            val wrongNumber = if (isKnown) 0 else 1

            runCatching {
                when (card.type) {
                    StudyCardType.WORD -> {
                        val current = runCatching { getWordProgressUseCase(card.itemId) }.getOrNull()
                        updateWordProgressUseCase(
                            id = card.itemId,
                            status = current?.status ?: "learning",
                            correctNumber = maxOf(correctNumber, current?.correctNumber ?: 0),
                            wrongNumber = maxOf(wrongNumber, current?.wrongNumber ?: 0),
                            repetitionLevel = maxOf(0, current?.repetitionLevel ?: 0),
                            lastReviewAt = now.toString(),
                            nextReviewAt = chooseNextReviewAt(
                                currentNextReviewAt = current?.nextReviewAt,
                                candidateNextReviewAt = nextReviewAt.toString()
                            )
                        )
                    }

                    StudyCardType.KANJI -> {
                        val current = runCatching { getKanjiProgressUseCase(card.itemId) }.getOrNull()
                        updateKanjiProgressUseCase(
                            id = card.itemId,
                            status = current?.status ?: "learning",
                            correctNumber = maxOf(correctNumber, current?.correctNumber ?: 0),
                            wrongNumber = maxOf(wrongNumber, current?.wrongNumber ?: 0),
                            repetitionLevel = maxOf(0, current?.repetitionLevel ?: 0),
                            lastReviewAt = now.toString(),
                            nextReviewAt = chooseNextReviewAt(
                                currentNextReviewAt = current?.nextReviewAt,
                                candidateNextReviewAt = nextReviewAt.toString()
                            )
                        )
                    }
                }
            }
                .onSuccess {
                    _uiState.update { state ->
                        val nextIndex = (state.study.currentIndex + 1).coerceAtMost(state.study.cards.lastIndex)
                        state.copy(
                            study = state.study.copy(
                                currentIndex = nextIndex,
                                isAnswerRevealed = false,
                                isSubmittingProgress = false,
                                submitErrorMessage = null
                            )
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            study = it.study.copy(
                                isSubmittingProgress = false,
                                submitErrorMessage = throwable.message ?: "Не удалось обновить прогресс"
                            )
                        )
                    }
                }
        }
    }

    private fun chooseNextReviewAt(
        currentNextReviewAt: String?,
        candidateNextReviewAt: String
    ): String {
        val currentMillis = currentNextReviewAt.toEpochMillisOrNull()
        val candidateMillis = candidateNextReviewAt.toEpochMillisOrNull()
        if (currentMillis == null || candidateMillis == null) return candidateNextReviewAt
        return if (currentMillis > candidateMillis) {
            currentNextReviewAt ?: candidateNextReviewAt
        } else {
            candidateNextReviewAt
        }
    }
}
