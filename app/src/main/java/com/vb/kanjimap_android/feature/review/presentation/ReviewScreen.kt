package com.vb.kanjimap_android.feature.review.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.BodyText
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenTitleText
import com.vb.kanjimap_android.core.ui.components.SecondaryButton
import com.vb.kanjimap_android.core.ui.components.SectionCard
import com.vb.kanjimap_android.core.ui.components.SectionTitleText
import com.vb.kanjimap_android.core.ui.theme.Dimens
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
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isGuest -> GuestState(onAuthClick = onAuthClick, onClose = onClose)
            uiState.isLoading -> LoadingView(message = "Загружаем повторение")
            uiState.errorMessage != null -> ErrorView(
                message = uiState.errorMessage,
                retryLabel = "Повторить",
                onRetry = onRetry,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.screenHorizontalPadding)
            )
            uiState.isCompleted -> CompletedState(onGoHome = onGoHome, onClose = onClose)
            uiState.isEmpty -> EmptyState(onGoHome = onGoHome)
            else -> {
                val item = uiState.currentItem ?: return@Surface
                ReviewCardState(
                    uiState = uiState,
                    item = item,
                    onShowAnswer = onShowAnswer,
                    onAnswerChange = onAnswerChange,
                    onSubmitAnswer = onSubmitAnswer,
                    onNextCard = onNextCard
                )
            }
        }
    }
}

@Composable
private fun GuestState(
    onAuthClick: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.screenContentPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
    ) {
        ScreenTitleText("Повторение")
        MetaText(
            text = "Повторение доступно после входа в аккаунт.",
            modifier = Modifier.fillMaxWidth()
        )
        PrimaryButton(onClick = onAuthClick, modifier = Modifier.fillMaxWidth()) { Text("Авторизоваться") }
        SecondaryButton(onClick = onClose, modifier = Modifier.fillMaxWidth()) { Text("Закрыть") }
    }
}

@Composable
private fun EmptyState(onGoHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.screenContentPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
    ) {
        ScreenTitleText("На сегодня повторений нет")
        PrimaryButton(onClick = onGoHome, modifier = Modifier.fillMaxWidth()) { Text("На главную") }
    }
}

@Composable
private fun CompletedState(
    onGoHome: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.screenContentPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
    ) {
        ScreenTitleText("Повторение завершено")
        PrimaryButton(onClick = onGoHome, modifier = Modifier.fillMaxWidth()) { Text("На главную") }
        SecondaryButton(onClick = onClose, modifier = Modifier.fillMaxWidth()) { Text("Закрыть") }
    }
}

@Composable
private fun ReviewCardState(
    uiState: ReviewUiState,
    item: ReviewItem,
    onShowAnswer: () -> Unit,
    onAnswerChange: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextCard: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.screenContentPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
    ) {
        MetaText(uiState.progressText)
        ScreenTitleText(
            text = when (item.itemType) {
                ReviewItemType.WORD -> item.word?.writingForm ?: "Слово"
                ReviewItemType.KANJI -> item.kanji?.literal ?: "Кандзи"
            }
        )

        if (uiState.isAnswerRevealed) {
            SectionCard(title = "Подсказки") {
                when (item.itemType) {
                    ReviewItemType.WORD -> {
                        BodyText("Чтение: ${item.word?.readingKana.orEmpty()}")
                        item.word?.topicName?.let { MetaText("Тема: $it") }
                    }
                    ReviewItemType.KANJI -> {
                        item.kanji?.strokeCount?.let { MetaText("Черт: $it") }
                        item.kanji?.jlptLevel?.let { MetaText("JLPT: $it") }
                    }
                }
            }
        } else {
            PrimaryButton(
                onClick = onShowAnswer,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Показать ответ") }
        }

        OutlinedTextField(
            value = uiState.answerInput,
            onValueChange = onAnswerChange,
            label = { Text("Ваш ответ") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSubmitting && uiState.result == null
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
            ResultBlock(
                result = uiState.result,
                onNextCard = onNextCard
            )
        }
    }
}

@Composable
private fun ResultBlock(
    result: ReviewResult,
    onNextCard: () -> Unit
) {
    SectionCard(title = if (result.isCorrect) "Правильно" else "Неправильно") {
        SectionTitleText(if (result.isCorrect) "Ответ принят" else "Ответ не принят")
        MetaText("acceptedAnswers: ${result.acceptedAnswers.joinToString()}")
        MetaText("status: ${result.status}")
        MetaText("nextReviewAt: ${result.nextReviewAt ?: "-"}")
        PrimaryButton(onClick = onNextCard, modifier = Modifier.fillMaxWidth()) {
            Text("Следующая карточка")
        }
    }
}
