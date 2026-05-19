package com.vb.kanjimap_android.feature.review.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.vb.kanjimap_android.core.ui.components.BodyText
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.core.ui.components.SecondaryButton
import com.vb.kanjimap_android.core.ui.components.SurfaceSection
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.feature.review.domain.model.ReviewItem
import com.vb.kanjimap_android.feature.review.domain.model.ReviewItemType
import com.vb.kanjimap_android.feature.review.domain.model.ReviewResult

@Composable
fun ReviewScreen(
    uiState: ReviewUiState,
    onAuthClick: () -> Unit,
    onClose: () -> Unit,
    onGoHome: () -> Unit,
    onRetry: () -> Unit,
    onShowAnswer: () -> Unit,
    onAnswerChange: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextCard: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    when {
        uiState.isGuest -> {
            ScreenList(modifier = modifier, contentPadding = contentPadding) {
                item {
                    ScreenHeader(
                        title = "Повторение",
                        subtitle = "Повторение доступно после входа в аккаунт"
                    )
                }
                item {
                    SurfaceSection(title = "Доступ") {
                        PrimaryButton(onClick = onAuthClick, modifier = Modifier.fillMaxWidth()) {
                            Text("Авторизоваться")
                        }
                        SecondaryButton(onClick = onClose, modifier = Modifier.fillMaxWidth()) {
                            Text("Закрыть")
                        }
                    }
                }
            }
        }

        uiState.isLoading -> LoadingView(message = "Загружаем повторение")

        uiState.errorMessage != null -> {
            ErrorView(
                message = uiState.errorMessage,
                retryLabel = "Повторить",
                onRetry = onRetry,
                modifier = Modifier.fillMaxWidth()
            )
        }

        uiState.isCompleted -> {
            ScreenList(modifier = modifier, contentPadding = contentPadding) {
                item {
                    ScreenHeader(
                        title = "Повторение завершено",
                        subtitle = "Все карточки на сегодня пройдены"
                    )
                }
                item {
                    SurfaceSection(title = "Готово") {
                        PrimaryButton(onClick = onGoHome, modifier = Modifier.fillMaxWidth()) {
                            Text("На главную")
                        }
                        SecondaryButton(onClick = onClose, modifier = Modifier.fillMaxWidth()) {
                            Text("Закрыть")
                        }
                    }
                }
            }
        }

        uiState.isEmpty -> {
            ScreenList(modifier = modifier, contentPadding = contentPadding) {
                item {
                    ScreenHeader(
                        title = "На сегодня повторений нет",
                        subtitle = "Возвращайтесь позже"
                    )
                }
                item {
                    SurfaceSection(title = "Повторение") {
                        PrimaryButton(onClick = onGoHome, modifier = Modifier.fillMaxWidth()) {
                            Text("На главную")
                        }
                    }
                }
            }
        }

        else -> {
            val item = uiState.currentItem ?: return
            ScreenList(modifier = modifier, contentPadding = contentPadding) {
                item {
                    ScreenHeader(
                        title = when (item.itemType) {
                            ReviewItemType.WORD -> item.word?.writingForm ?: "Слово"
                            ReviewItemType.KANJI -> item.kanji?.literal ?: "Кандзи"
                        },
                        subtitle = uiState.progressText
                    )
                }

                item {
                    ReviewPromptSection(
                        item = item,
                        isAnswerRevealed = uiState.isAnswerRevealed,
                        onShowAnswer = onShowAnswer
                    )
                }

                item {
                    SurfaceSection(title = "Ваш ответ") {
                        OutlinedTextField(
                            value = uiState.answerInput,
                            onValueChange = onAnswerChange,
                            label = { Text("Ваш ответ") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isSubmitting && uiState.result == null,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                        )

                        if (uiState.result == null) {
                            PrimaryButton(
                                onClick = onSubmitAnswer,
                                modifier = Modifier.fillMaxWidth(),
                                enabled = uiState.answerInput.isNotBlank() && !uiState.isSubmitting
                            ) {
                                Text("Проверить")
                            }
                        } else {
                            ReviewResultSection(
                                result = uiState.result,
                                onNextCard = onNextCard
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewPromptSection(
    item: ReviewItem,
    isAnswerRevealed: Boolean,
    onShowAnswer: () -> Unit
) {
    SurfaceSection(title = "Карточка") {
        when (item.itemType) {
            ReviewItemType.WORD -> {
                BodyText(item.word?.writingForm ?: "Слово")
                if (isAnswerRevealed) {
                    item.word?.readingKana?.let { MetaText("Чтение: $it") }
                    item.word?.topicName?.let { MetaText("Тема: $it") }
                    item.word?.jlptLevel?.let { MetaText("JLPT: $it") }
                }
            }

            ReviewItemType.KANJI -> {
                BodyText(item.kanji?.literal ?: "Кандзи")
                if (isAnswerRevealed) {
                    item.kanji?.strokeCount?.let { MetaText("Черт: $it") }
                    item.kanji?.jlptLevel?.let { MetaText("JLPT: $it") }
                }
            }
        }

        if (!isAnswerRevealed) {
            PrimaryButton(
                onClick = onShowAnswer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Показать ответ")
            }
        }
    }
}

@Composable
private fun ReviewResultSection(
    result: ReviewResult,
    onNextCard: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.sm)) {
        BodyText(if (result.isCorrect) "Ответ принят" else "Ответ не принят")
        MetaText("acceptedAnswers: ${result.acceptedAnswers.joinToString()}")
        MetaText("status: ${result.status}")
        MetaText("nextReviewAt: ${result.nextReviewAt ?: "-"}")
        PrimaryButton(onClick = onNextCard, modifier = Modifier.fillMaxWidth()) {
            Text("Следующая карточка")
        }
    }
}
