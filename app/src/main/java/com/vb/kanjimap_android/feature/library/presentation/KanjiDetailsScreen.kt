package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.feature.library.presentation.components.LibraryEmptyState
import com.vb.kanjimap_android.feature.library.presentation.components.LibrarySectionCard
import com.vb.kanjimap_android.feature.library.presentation.components.RelatedWordsColumn

@Composable
fun KanjiDetailsScreen(
    uiState: KanjiDetailsUiState,
    onRetry: () -> Unit,
    onSaveClick: () -> Unit,
    onWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingView(message = "Загружаем кандзи")
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

            uiState.item == null -> {
                LibraryEmptyState(
                    title = "Нет данных",
                    description = "Детали кандзи пока недоступны.",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = CoreSpacing.lg)
                )
            }

            else -> {
                val details = uiState.item
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = CoreSpacing.lg, vertical = CoreSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)
                        ) {
                            Text(
                                text = details.kanji.literal,
                                style = MaterialTheme.typography.displaySmall
                            )
                            Text(
                                text = buildString {
                                    append("ID ")
                                    append(details.kanji.kanjiId)
                                    details.kanji.strokeCount?.let {
                                        append(" • ")
                                        append(it)
                                        append(" черт")
                                    }
                                    details.kanji.jlptLevel?.let {
                                        append(" • ")
                                        append(it)
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    item {
                        Button(
                            onClick = onSaveClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Сохранить кандзи")
                        }
                    }

                    item {
                        LibrarySectionCard(title = "Значения") {
                            if (details.meanings.isEmpty()) {
                                Text(
                                    text = "Значения пока не указаны.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)) {
                                    details.meanings.forEach { meaning ->
                                        Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)) {
                                            Text(
                                                text = meaning.meaning,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Text(
                                                text = meaning.languageCode.uppercase(),
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            meaning.example?.let {
                                                Text(
                                                    text = it,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        LibrarySectionCard(title = "On readings") {
                            ReadingContent(details.onReadings)
                        }
                    }

                    item {
                        LibrarySectionCard(title = "Kun readings") {
                            ReadingContent(details.kunReadings)
                        }
                    }

                    item {
                        LibrarySectionCard(title = "Nanori readings") {
                            ReadingContent(details.nanoriReadings)
                        }
                    }

                    item {
                        LibrarySectionCard(title = "Слова с этим кандзи") {
                            if (details.words.isEmpty()) {
                                Text(
                                    text = "Слова пока не указаны.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                RelatedWordsColumn(
                                    words = details.words,
                                    onWordClick = onWordClick
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(CoreSpacing.xs)) }
                }
            }
        }
    }
}

@Composable
private fun ReadingContent(readings: List<String>) {
    if (readings.isEmpty()) {
        Text(
            text = "Нет данных",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }

    Text(
        text = readings.joinToString(" • "),
        style = MaterialTheme.typography.bodyLarge
    )
}
