package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.core.ui.components.SecondaryButton
import com.vb.kanjimap_android.core.ui.components.SurfaceSection
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningEmptyState
import com.vb.kanjimap_android.feature.learning.presentation.components.StudyCardView

@Composable
fun StudyScreen(
    uiState: StudyUiState,
    onBackClick: () -> Unit,
    onShowAnswer: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onKnownClick: () -> Unit,
    onUnknownClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    when {
        uiState.isLoading -> LoadingView(message = "Подготавливаем карточки")
        uiState.errorMessage != null -> {
            ErrorView(
                message = uiState.errorMessage,
                retryLabel = "Повторить",
                onRetry = onRetry,
                modifier = Modifier.fillMaxWidth()
            )
        }
        uiState.currentCard == null -> {
            LearningEmptyState(
                title = "Карточек пока нет",
                description = "Для выбранного режима обучения ничего не найдено.",
                modifier = Modifier.fillMaxWidth()
            )
        }
        else -> {
            val currentCard = uiState.currentCard ?: return

            ScreenList(
                modifier = modifier,
                contentPadding = contentPadding
            ) {
                item {
                    ScreenHeader(
                        title = "Изучение",
                        subtitle = uiState.progressText,
                        onBackClick = onBackClick
                    )
                }

                item {
                    StudyCardView(
                        card = currentCard,
                        isAnswerVisible = uiState.isAnswerRevealed,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    SurfaceSection(title = "Действия") {
                        if (uiState.submitErrorMessage != null) {
                            MetaText(text = uiState.submitErrorMessage)
                        }

                        PrimaryButton(
                            onClick = onShowAnswer,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isAnswerRevealed && !uiState.isSubmittingProgress
                        ) {
                            Text("Показать ответ")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SecondaryButton(
                                onClick = onPreviousClick,
                                modifier = Modifier.weight(1f),
                                enabled = uiState.currentIndex > 0 && !uiState.isSubmittingProgress
                            ) {
                                Text("Назад")
                            }
                            SecondaryButton(
                                onClick = onNextClick,
                                modifier = Modifier.weight(1f),
                                enabled = uiState.currentIndex < uiState.cards.lastIndex &&
                                    !uiState.isSubmittingProgress
                            ) {
                                Text("Дальше")
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SecondaryButton(
                                onClick = onUnknownClick,
                                modifier = Modifier.weight(1f),
                                enabled = uiState.isAnswerRevealed && !uiState.isSubmittingProgress
                            ) {
                                Text("Не знал")
                            }
                            PrimaryButton(
                                onClick = onKnownClick,
                                modifier = Modifier.weight(1f),
                                enabled = uiState.isAnswerRevealed && !uiState.isSubmittingProgress
                            ) {
                                Text("Знал")
                            }
                        }
                    }
                }
            }
        }
    }
}
