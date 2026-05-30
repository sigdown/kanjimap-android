package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.BodyText
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.core.ui.components.SurfaceSection
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.feature.library.presentation.components.LibraryEmptyState
import com.vb.kanjimap_android.feature.library.presentation.components.RelatedWordsColumn

@Composable
fun KanjiDetailsScreen(
    uiState: KanjiDetailsUiState,
    onRetry: () -> Unit,
    onSaveClick: () -> Unit,
    onWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    when {
        uiState.isLoading -> LoadingView(message = "Загружаем кандзи")
        uiState.errorMessage != null -> {
            ErrorView(
                message = uiState.errorMessage,
                retryLabel = "Повторить",
                onRetry = onRetry,
                modifier = Modifier.fillMaxWidth()
            )
        }
        uiState.item == null -> {
            LibraryEmptyState(
                title = "Нет данных",
                description = "Детали кандзи пока недоступны.",
                modifier = Modifier.fillMaxWidth()
            )
        }
        else -> {
            val details = uiState.item

            ScreenList(
                modifier = modifier,
                contentPadding = contentPadding
            ) {
                item {
                    ScreenHeader(
                        title = details.kanji.literal,
                        subtitle = buildString {
                            append("ID ${details.kanji.kanjiId}")
                            details.kanji.strokeCount?.let {
                                append(" • $it черт")
                            }
                            details.kanji.jlptLevel?.let {
                                append(" • $it")
                            }
                        }
                    )
                }

                item {
                    PrimaryButton(
                        onClick = onSaveClick,
                        enabled = !uiState.isSaved,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.isSaved) "Кандзи сохранено" else "Сохранить кандзи")
                    }
                }

                item {
                    SurfaceSection(title = "Значения") {
                        if (details.meanings.isEmpty()) {
                            MetaText("Значения пока не указаны.")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)) {
                                details.meanings.forEach { meaning ->
                                    Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)) {
                                        BodyText(meaning.meaning)
                                        MetaText(meaning.languageCode.uppercase())
                                        meaning.example?.let { MetaText(it) }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    SurfaceSection(title = "On readings") {
                        ReadingContent(details.onReadings)
                    }
                }

                item {
                    SurfaceSection(title = "Kun readings") {
                        ReadingContent(details.kunReadings)
                    }
                }

                item {
                    SurfaceSection(title = "Nanori readings") {
                        ReadingContent(details.nanoriReadings)
                    }
                }

                item {
                    SurfaceSection(title = "Слова с этим кандзи") {
                        if (details.words.isEmpty()) {
                            MetaText("Слова пока не указаны.")
                        } else {
                            RelatedWordsColumn(
                                words = details.words
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReadingContent(readings: List<String>) {
    if (readings.isEmpty()) {
        MetaText("Нет данных")
    } else {
        BodyText(readings.joinToString(" • "))
    }
}
