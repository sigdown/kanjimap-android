package com.vb.kanjimap_android.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.core.ui.theme.Dimens
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
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingView(message = "Загружаем Home")
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

            uiState.isGuest -> GuestHomeContent(
                onAuthClick = onAuthClick,
                modifier = modifier,
                contentPadding = contentPadding
            )
            else -> {
                val summary = uiState.summary ?: return@Surface
                HomeSummaryContent(
                    summary = summary,
                    onOpenReview = onOpenReview,
                    onOpenBlocks = onOpenBlocks,
                    onOpenWords = onOpenWords,
                    onOpenKanji = onOpenKanji,
                    modifier = modifier,
                    contentPadding = contentPadding
                )
            }
        }
    }
}

@Composable
private fun GuestHomeContent(
    onAuthClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(Dimens.screenContentPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
    ) {
        ScreenTitleText("Главная")
        MetaText(
            text = "Home — это персональная зона обучения с повторами и быстрым доступом к прогрессу.",
            modifier = Modifier.fillMaxWidth()
        )
        PrimaryButton(onClick = onAuthClick, modifier = Modifier.fillMaxWidth()) {
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
    onOpenKanji: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(Dimens.screenContentPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
    ) {
        item {
            ScreenTitleText("Привет, ${summary.username}")
        }

        item {
            HomeCard(title = "На сегодня") {
                BodyText("Слова на повторение: ${summary.reviewWordsCount}")
                BodyText("Кандзи на повторение: ${summary.reviewKanjiCount}")
                SectionTitleText("Всего на сегодня: ${summary.totalReviewCount}")
                PrimaryButton(onClick = onOpenReview, modifier = Modifier.fillMaxWidth()) {
                    Text("Начать повторение")
                }
            }
        }

        item {
            HomeCard(title = "Обучение") {
                if (summary.blocksPreview.isEmpty()) {
                    MetaText("Пока нет доступных блоков.")
                } else {
                    summary.blocksPreview.forEach { block ->
                        BlockPreviewItem(block = block)
                    }
                }
                SecondaryButton(onClick = onOpenBlocks, modifier = Modifier.fillMaxWidth()) {
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
                    SecondaryButton(
                        onClick = onOpenWords,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Слова")
                    }
                    SecondaryButton(
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
    SectionCard(title = title) { content() }
}

@Composable
private fun BlockPreviewItem(block: HomeBlockPreview) {
    BodyText("• ${block.title} (${block.blockType})")
}
