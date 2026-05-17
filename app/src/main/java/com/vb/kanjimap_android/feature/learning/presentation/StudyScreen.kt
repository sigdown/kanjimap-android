package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningEmptyState
import com.vb.kanjimap_android.feature.learning.presentation.components.StudyCardView

@Composable
fun StudyScreen(
    uiState: StudyUiState,
    onShowAnswer: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingView(message = "Подготавливаем карточки")
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

            uiState.currentCard == null -> {
                LearningEmptyState(
                    title = "Карточек пока нет",
                    description = "Для выбранного режима обучения ничего не найдено.",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = CoreSpacing.lg)
                )
            }

            else -> {
                val currentCard = uiState.currentCard ?: return@Surface
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = CoreSpacing.lg, vertical = CoreSpacing.md),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(CoreSpacing.lg)
                ) {
                    Text(
                        text = uiState.progressText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    StudyCardView(
                        card = currentCard,
                        isAnswerVisible = uiState.isAnswerRevealed,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
                    ) {
                        Button(
                            onClick = onShowAnswer,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isAnswerRevealed
                        ) {
                            Text("Показать ответ")
                        }

                        OutlinedButton(
                            onClick = onPreviousClick,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.currentIndex > 0
                        ) {
                            Text("Назад")
                        }

                        Button(
                            onClick = onNextClick,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.currentIndex < uiState.cards.lastIndex
                        ) {
                            Text("Дальше")
                        }
                    }
                }
            }
        }
    }
}
