package com.vb.kanjimap_android.feature.review.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
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
                    .padding(horizontal = CoreSpacing.lg)
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
            .padding(CoreSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)
    ) {
        Text("Повторение", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
        Text(
            text = "Повторение доступно после входа в аккаунт.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onAuthClick) { Text("Авторизоваться") }
        OutlinedButton(onClick = onClose) { Text("Закрыть") }
    }
}

@Composable
private fun EmptyState(onGoHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CoreSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)
    ) {
        Text("На сегодня повторений нет", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = onGoHome) { Text("На главную") }
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
            .padding(CoreSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)
    ) {
        Text("Повторение завершено", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = onGoHome) { Text("На главную") }
        OutlinedButton(onClick = onClose) { Text("Закрыть") }
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
            .padding(CoreSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)
    ) {
        Text(uiState.progressText, style = MaterialTheme.typography.titleMedium)
        Text(
            text = when (item.itemType) {
                ReviewItemType.WORD -> item.word?.writingForm ?: "Слово"
                ReviewItemType.KANJI -> item.kanji?.literal ?: "Кандзи"
            },
            style = MaterialTheme.typography.displaySmall
        )

        if (uiState.isAnswerRevealed) {
            when (item.itemType) {
                ReviewItemType.WORD -> {
                    Text("Чтение: ${item.word?.readingKana.orEmpty()}")
                    item.word?.topicName?.let { Text("Тема: $it") }
                }
                ReviewItemType.KANJI -> {
                    item.kanji?.strokeCount?.let { Text("Черт: $it") }
                    item.kanji?.jlptLevel?.let { Text("JLPT: $it") }
                }
            }
        } else {
            Button(
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
            Button(
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
    ) {
        Text(
            text = if (result.isCorrect) "Правильно" else "Неправильно",
            color = if (result.isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleLarge
        )
        Text("acceptedAnswers: ${result.acceptedAnswers.joinToString()}")
        Text("status: ${result.status}")
        Text("nextReviewAt: ${result.nextReviewAt ?: "-"}")
        Button(onClick = onNextCard, modifier = Modifier.fillMaxWidth()) {
            Text("Следующая карточка")
        }
    }
}
