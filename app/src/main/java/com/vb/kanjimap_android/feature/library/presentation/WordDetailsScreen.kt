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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.feature.library.presentation.components.KanjisRow
import com.vb.kanjimap_android.feature.library.presentation.components.LibraryEmptyState
import com.vb.kanjimap_android.feature.library.presentation.components.LibrarySectionCard
import com.vb.kanjimap_android.feature.library.presentation.components.RelatedWordsColumn

@Composable
fun WordDetailsScreen(
    uiState: WordDetailsUiState,
    onRetry: () -> Unit,
    onSaveClick: () -> Unit,
    onKanjiClick: (Long) -> Unit,
    onRelatedWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingView(message = "Загружаем слово")
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
                    description = "Детали слова пока недоступны.",
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
                                text = details.word.writingForm,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = details.word.readingKana,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            val meta = listOfNotNull(details.word.jlptLevel, details.word.topicName)
                            if (meta.isNotEmpty()) {
                                Text(
                                    text = meta.joinToString(" • "),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = onSaveClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Сохранить слово")
                        }
                    }

                    item {
                        LibrarySectionCard(title = "Значения") {
                            Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)) {
                                details.meanings.forEachIndexed { index, meaning ->
                                    Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)) {
                                        Text(
                                            text = "${index + 1}. ${meaning.meaning}",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        meaning.partOfSpeech?.let {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        meaning.exampleJp?.let {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                        meaning.exampleTranslation?.let {
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

                    item {
                        LibrarySectionCard(title = "Кандзи в слове") {
                            if (details.kanjis.isEmpty()) {
                                Text(
                                    text = "Для этого слова кандзи не указаны.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                KanjisRow(
                                    kanjis = details.kanjis,
                                    onKanjiClick = onKanjiClick
                                )
                            }
                        }
                    }

                    item {
                        LibrarySectionCard(title = "Связанные слова") {
                            if (details.relatedWords.isEmpty()) {
                                Text(
                                    text = "Связанных слов пока нет.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)) {
                                    details.relatedWords.forEach { relatedWord ->
                                        Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)) {
                                            Text(
                                                text = relatedWord.relationType,
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            relatedWord.note?.let {
                                                Text(
                                                    text = it,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            relatedWord.word?.let { word ->
                                                RelatedWordsColumn(
                                                    words = listOf(word),
                                                    onWordClick = onRelatedWordClick
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(CoreSpacing.xs)) }
                }
            }
        }
    }
}
