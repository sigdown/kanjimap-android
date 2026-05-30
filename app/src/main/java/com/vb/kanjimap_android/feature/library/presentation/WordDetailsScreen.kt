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
import com.vb.kanjimap_android.feature.library.presentation.components.KanjisRow
import com.vb.kanjimap_android.feature.library.presentation.components.LibraryEmptyState
import com.vb.kanjimap_android.feature.library.presentation.components.RelatedWordsColumn

@Composable
fun WordDetailsScreen(
    uiState: WordDetailsUiState,
    onRetry: () -> Unit,
    onSaveClick: () -> Unit,
    onKanjiClick: (Long) -> Unit,
    onRelatedWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    when {
        uiState.isLoading -> LoadingView(message = "Загружаем слово")
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
                description = "Детали слова пока недоступны.",
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
                        title = details.word.writingForm,
                        subtitle = buildString {
                            append(details.word.readingKana)
                            val meta = listOfNotNull(details.word.jlptLevel, details.word.topicName)
                            if (meta.isNotEmpty()) {
                                append(" • ")
                                append(meta.joinToString(" • "))
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
                        Text(if (uiState.isSaved) "Слово сохранено" else "Сохранить слово")
                    }
                }

                item {
                    SurfaceSection(title = "Значения") {
                        Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)) {
                            details.meanings.forEachIndexed { index, meaning ->
                                Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)) {
                                    BodyText("${index + 1}. ${meaning.meaning}")
                                    meaning.partOfSpeech?.let { MetaText(it) }
                                    meaning.exampleJp?.let { BodyText(it) }
                                    meaning.exampleTranslation?.let { MetaText(it) }
                                }
                            }
                        }
                    }
                }

                item {
                    SurfaceSection(title = "Кандзи в слове") {
                        if (details.kanjis.isEmpty()) {
                            MetaText("Для этого слова кандзи не указаны.")
                        } else {
                            KanjisRow(
                                kanjis = details.kanjis
                            )
                        }
                    }
                }

                item {
                    SurfaceSection(title = "Связанные слова") {
                        if (details.relatedWords.isEmpty()) {
                            MetaText("Связанных слов пока нет.")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)) {
                                details.relatedWords.forEach { relatedWord ->
                                    Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)) {
                                        BodyText(relatedWord.relationType)
                                        relatedWord.note?.let { MetaText(it) }
                                        relatedWord.word?.let { word ->
                                            RelatedWordsColumn(
                                                words = listOf(word)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
