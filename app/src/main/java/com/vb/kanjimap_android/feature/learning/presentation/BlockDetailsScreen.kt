package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.feature.learning.presentation.components.KanjiChips
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningEmptyState
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningSectionCard
import com.vb.kanjimap_android.feature.learning.presentation.components.StudyModeButtons
import com.vb.kanjimap_android.feature.learning.presentation.components.WordsChips

@Composable
fun BlockDetailsScreen(
    uiState: BlockDetailsUiState,
    onRetry: () -> Unit,
    onStudyWordsClick: () -> Unit,
    onStudyKanjiClick: () -> Unit,
    onStudyAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingView(message = "Загружаем блок")
            uiState.errorMessage != null -> {
                ErrorView(
                    message = uiState.errorMessage,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = CoreSpacing.lg),
                    retryLabel = "Повторить",
                    onRetry = onRetry
                )
            }

            uiState.item == null -> {
                LearningEmptyState(
                    title = "Нет данных по блоку",
                    description = "Попробуйте открыть блок позже.",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = CoreSpacing.lg)
                )
            }

            else -> {
                val details = uiState.item
                val canStudyWords = details.words.isNotEmpty()
                val canStudyKanji = details.kanjis.isNotEmpty()
                val canStudyAll = canStudyWords && canStudyKanji

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = CoreSpacing.lg, vertical = CoreSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)
                ) {
                    item {
                        Text(
                            text = details.block.title,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    item {
                        LearningSectionCard(title = "О блоке") {
                            details.block.description?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Text(
                                text = "Тип: ${details.block.blockType}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    item {
                        StudyModeButtons(
                            showWords = canStudyWords,
                            showKanji = canStudyKanji,
                            showAll = canStudyAll,
                            onWordsClick = onStudyWordsClick,
                            onKanjiClick = onStudyKanjiClick,
                            onAllClick = onStudyAllClick
                        )
                    }

                    item {
                        LearningSectionCard(title = "Слова") {
                            if (details.words.isEmpty()) {
                                Text(
                                    text = "Слова в этом блоке пока не добавлены.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                WordsChips(words = details.words)
                            }
                        }
                    }

                    item {
                        LearningSectionCard(title = "Кандзи") {
                            if (details.kanjis.isEmpty()) {
                                Text(
                                    text = "Кандзи в этом блоке пока не добавлены.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                KanjiChips(kanjis = details.kanjis)
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(CoreSpacing.xs)) }
                }
            }
        }
    }
}
