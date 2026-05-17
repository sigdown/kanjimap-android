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
import com.vb.kanjimap_android.core.ui.components.SectionTitleText
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.core.ui.theme.Dimens
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
                        .padding(horizontal = Dimens.screenHorizontalPadding),
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
                        .padding(horizontal = Dimens.screenHorizontalPadding)
                )
            }

            else -> {
                val details = uiState.item
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(Dimens.screenContentPadding),
                    verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)
                        ) {
                            ScreenTitleText(details.word.writingForm)
                            MetaText(details.word.readingKana)
                            val meta = listOfNotNull(details.word.jlptLevel, details.word.topicName)
                            if (meta.isNotEmpty()) {
                                MetaText(meta.joinToString(" • "))
                            }
                        }
                    }

                    item {
                        PrimaryButton(
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
                                        BodyText("${index + 1}. ${meaning.meaning}")
                                        meaning.partOfSpeech?.let {
                                            MetaText(it)
                                        }
                                        meaning.exampleJp?.let {
                                            BodyText(it)
                                        }
                                        meaning.exampleTranslation?.let {
                                            MetaText(it)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        LibrarySectionCard(title = "Кандзи в слове") {
                            if (details.kanjis.isEmpty()) {
                                MetaText("Для этого слова кандзи не указаны.")
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
                                MetaText("Связанных слов пока нет.")
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)) {
                                    details.relatedWords.forEach { relatedWord ->
                                        Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)) {
                                            SectionTitleText(relatedWord.relationType)
                                            relatedWord.note?.let {
                                                MetaText(it)
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
