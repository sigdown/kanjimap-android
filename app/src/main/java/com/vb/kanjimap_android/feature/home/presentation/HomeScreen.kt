package com.vb.kanjimap_android.feature.home.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenTitleText
import com.vb.kanjimap_android.core.ui.components.SecondaryButton
import com.vb.kanjimap_android.core.ui.theme.Dimens
import com.vb.kanjimap_android.feature.home.domain.model.HomeBlockPreview
import com.vb.kanjimap_android.feature.home.domain.model.HomeSummary

private val ScreenVerticalPadding = 20.dp
private val SectionGap = 28.dp
private val SectionContentGap = 12.dp
private val CardCorner = 18.dp
private val MetricGap = 12.dp

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
    Surface(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                LoadingView(message = "Загружаем Home")
            }

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

            uiState.isGuest -> {
                GuestHomeContent(
                    onAuthClick = onAuthClick,
                    contentPadding = contentPadding
                )
            }

            else -> {
                val summary = uiState.summary ?: return@Surface
                HomeSummaryContent(
                    summary = summary,
                    onOpenReview = onOpenReview,
                    onOpenBlocks = onOpenBlocks,
                    onOpenWords = onOpenWords,
                    onOpenKanji = onOpenKanji,
                    contentPadding = contentPadding
                )
            }
        }
    }
}

@Composable
private fun GuestHomeContent(
    onAuthClick: () -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Dimens.screenHorizontalPadding,
            end = Dimens.screenHorizontalPadding,
            top = ScreenVerticalPadding + contentPadding.calculateTopPadding(),
            bottom = ScreenVerticalPadding + contentPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(SectionGap)
    ) {
        item {
            HomeHeader(
                username = "друг",
                subtitle = "Персональная зона обучения и повторения"
            )
        }

        item {
            HomeSection(title = "Главная") {
                MetaText("Home — это персональная зона обучения с повторами и быстрым доступом к словарям.")
                PrimaryButton(
                    onClick = onAuthClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Авторизоваться")
                }
            }
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
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Dimens.screenHorizontalPadding,
            end = Dimens.screenHorizontalPadding,
            top = ScreenVerticalPadding + contentPadding.calculateTopPadding(),
            bottom = ScreenVerticalPadding + contentPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(SectionGap)
    ) {
        item {
            HomeHeader(
                username = summary.username,
                subtitle = "Ваши повторения и обучение на сегодня"
            )
        }

        item {
            HomeSection(title = "На сегодня") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MetricGap)
                ) {
                    MetricCard(
                        value = summary.reviewWordsCount.toString(),
                        label = "Слова\nна повторение",
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        value = summary.reviewKanjiCount.toString(),
                        label = "Кандзи\nна повторение",
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        value = summary.totalReviewCount.toString(),
                        label = "Всего\nна сегодня",
                        modifier = Modifier.weight(1f),
                        emphasize = true
                    )
                }

                PrimaryButton(
                    onClick = onOpenReview,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Начать повторение")
                }
            }
        }

        item {
            HomeSection(title = "Обучение") {
                if (summary.blocksPreview.isEmpty()) {
                    MetaText("Пока нет доступных блоков.")
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        summary.blocksPreview.take(3).forEachIndexed { index, block ->
                            BlockPreviewRow(block)
                            if (index < minOf(summary.blocksPreview.size, 3) - 1) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
                                )
                            }
                        }
                    }
                }

                SecondaryButton(
                    onClick = onOpenBlocks,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Открыть блоки")
                }
            }
        }

        item {
            HomeSection(title = "Быстрый доступ") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MetricGap)
                ) {
                    QuickAccessButton(
                        label = "Слова",
                        onClick = onOpenWords,
                        modifier = Modifier.weight(1f)
                    )
                    QuickAccessButton(
                        label = "Кандзи",
                        onClick = onOpenKanji,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    username: String,
    subtitle: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ScreenTitleText("Привет, $username")
        MetaText(subtitle)
    }
}

@Composable
private fun HomeSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SectionContentGap)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        content()
    }
}

@Composable
private fun MetricCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    emphasize: Boolean = false
) {
    Surface(
        modifier = modifier.heightIn(min = 156.dp),
        shape = RoundedCornerShape(CardCorner),
        color = if (emphasize) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (emphasize) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = if (emphasize) {
                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.82f)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun BlockPreviewRow(block: HomeBlockPreview) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = block.title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Surface(
            shape = RoundedCornerShape(999.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text(
                text = block.blockType.lowercase(),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuickAccessButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.heightIn(min = 64.dp),
        onClick = onClick,
        shape = RoundedCornerShape(CardCorner),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}