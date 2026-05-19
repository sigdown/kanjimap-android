package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.core.ui.components.SurfaceSection
import com.vb.kanjimap_android.feature.learning.presentation.components.KanjiChips
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningEmptyState
import com.vb.kanjimap_android.feature.learning.presentation.components.StudyModeButtons
import com.vb.kanjimap_android.feature.learning.presentation.components.WordsChips

@Composable
fun BlockDetailsScreen(
    uiState: BlockDetailsUiState,
    onRetry: () -> Unit,
    onStudyWordsClick: () -> Unit,
    onStudyKanjiClick: () -> Unit,
    onStudyAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    when {
        uiState.isLoading -> LoadingView(message = "Загружаем блок")
        uiState.errorMessage != null -> {
            ErrorView(
                message = uiState.errorMessage,
                retryLabel = "Повторить",
                onRetry = onRetry,
                modifier = Modifier.fillMaxWidth()
            )
        }
        uiState.item == null -> {
            LearningEmptyState(
                title = "Нет данных по блоку",
                description = "Попробуйте открыть блок позже.",
                modifier = Modifier.fillMaxWidth()
            )
        }
        else -> {
            val details = uiState.item
            val canStudyWords = details.words.isNotEmpty()
            val canStudyKanji = details.kanjis.isNotEmpty()
            val canStudyAll = canStudyWords && canStudyKanji

            ScreenList(
                modifier = modifier,
                contentPadding = contentPadding
            ) {
                item {
                    ScreenHeader(
                        title = details.block.title,
                        subtitle = details.block.description
                    )
                }

                item {
                    SurfaceSection(title = "О блоке") {
                        MetaText("Тип: ${details.block.blockType}")
                        MetaText("Порядок: ${details.block.orderIndex + 1}")
                    }
                }

                item {
                    SurfaceSection(title = "Режим изучения") {
                        StudyModeButtons(
                            showWords = canStudyWords,
                            showKanji = canStudyKanji,
                            showAll = canStudyAll,
                            onWordsClick = onStudyWordsClick,
                            onKanjiClick = onStudyKanjiClick,
                            onAllClick = onStudyAllClick
                        )
                    }
                }

                item {
                    SurfaceSection(title = "Слова") {
                        if (details.words.isEmpty()) {
                            MetaText("Слова в этом блоке пока не добавлены.")
                        } else {
                            WordsChips(words = details.words)
                        }
                    }
                }

                item {
                    SurfaceSection(title = "Кандзи") {
                        if (details.kanjis.isEmpty()) {
                            MetaText("Кандзи в этом блоке пока не добавлены.")
                        } else {
                            KanjiChips(kanjis = details.kanjis)
                        }
                    }
                }
            }
        }
    }
}
