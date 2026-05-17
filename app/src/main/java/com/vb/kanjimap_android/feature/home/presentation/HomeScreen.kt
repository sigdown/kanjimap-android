package com.vb.kanjimap_android.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.feature.home.domain.model.HomeBlockPreview
import com.vb.kanjimap_android.feature.home.domain.model.HomeSummary

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAuthClick: () -> Unit,
    onOpenReview: () -> Unit,
    onOpenBlocks: () -> Unit,
    onOpenWords: () -> Unit,
    onOpenKanji: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingView(message = "Загружаем Home")
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

            uiState.isGuest -> GuestHomeContent(onAuthClick = onAuthClick)
            else -> {
                val summary = uiState.summary ?: return@Surface
                HomeSummaryContent(
                    summary = summary,
                    onOpenReview = onOpenReview,
                    onOpenBlocks = onOpenBlocks,
                    onOpenWords = onOpenWords,
                    onOpenKanji = onOpenKanji
                )
            }
        }
    }
}

@Composable
private fun GuestHomeContent(
    onAuthClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = CoreSpacing.lg, vertical = CoreSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)
    ) {
        Text(
            text = "Главная",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Home — это персональная зона обучения с повторами и быстрым доступом к прогрессу.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onAuthClick) {
            Text("Авторизоваться")
        }
    }
}

@Composable
private fun HomeSummaryContent(
    summary: HomeSummary,
    onOpenReview: () -> Unit,
    onOpenBlocks: () -> Unit,
    onOpenWords: () -> Unit,
    onOpenKanji: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = CoreSpacing.lg, vertical = CoreSpacing.md),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)
    ) {
        item {
            Text(
                text = "Привет, ${summary.username}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            HomeCard(title = "На сегодня") {
                Text("Слова на повторение: ${summary.reviewWordsCount}")
                Text("Кандзи на повторение: ${summary.reviewKanjiCount}")
                Text(
                    text = "Всего на сегодня: ${summary.totalReviewCount}",
                    style = MaterialTheme.typography.titleMedium
                )
                Button(onClick = onOpenReview, modifier = Modifier.fillMaxWidth()) {
                    Text("Начать повторение")
                }
            }
        }

        item {
            HomeCard(title = "Обучение") {
                if (summary.blocksPreview.isEmpty()) {
                    Text(
                        text = "Пока нет доступных блоков.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    summary.blocksPreview.forEach { block ->
                        BlockPreviewItem(block = block)
                    }
                }
                OutlinedButton(onClick = onOpenBlocks, modifier = Modifier.fillMaxWidth()) {
                    Text("Открыть блоки")
                }
            }
        }

        item {
            HomeCard(title = "Быстрый доступ") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
                ) {
                    OutlinedButton(
                        onClick = onOpenWords,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Слова")
                    }
                    OutlinedButton(
                        onClick = onOpenKanji,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Кандзи")
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CoreSpacing.md),
            verticalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            content()
        }
    }
}

@Composable
private fun BlockPreviewItem(block: HomeBlockPreview) {
    Text(
        text = "• ${block.title} (${block.blockType})",
        style = MaterialTheme.typography.bodyLarge
    )
}
