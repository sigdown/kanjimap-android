package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.SecondaryButton
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.core.ui.theme.Dimens
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningEmptyState
import com.vb.kanjimap_android.feature.learning.presentation.components.StudyCardView

@Composable
fun StudyScreen(
    uiState: StudyUiState,
    onShowAnswer: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onKnownClick: () -> Unit,
    onUnknownClick: () -> Unit,
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
                        .padding(horizontal = Dimens.screenHorizontalPadding),
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
                        .padding(horizontal = Dimens.screenHorizontalPadding)
                )
            }

            else -> {
                val currentCard = uiState.currentCard ?: return@Surface
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(Dimens.screenContentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
                ) {
                    MetaText(text = uiState.progressText)

                    StudyCardView(
                        card = currentCard,
                        isAnswerVisible = uiState.isAnswerRevealed,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Dimens.cardInnerSpacing)
                    ) {
                        PrimaryButton(
                            onClick = onShowAnswer,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isAnswerRevealed && !uiState.isSubmittingProgress
                        ) {
                            Text("Показать ответ")
                        }

                        SecondaryButton(
                            onClick = onPreviousClick,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.currentIndex > 0 && !uiState.isSubmittingProgress
                        ) {
                            Text("Назад")
                        }

                        PrimaryButton(
                            onClick = onNextClick,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.currentIndex < uiState.cards.lastIndex &&
                                !uiState.isSubmittingProgress
                        ) {
                            Text("Дальше")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
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

                        if (uiState.submitErrorMessage != null) {
                            MetaText(text = uiState.submitErrorMessage)
                        }
                    }
                }
            }
        }
    }
}
