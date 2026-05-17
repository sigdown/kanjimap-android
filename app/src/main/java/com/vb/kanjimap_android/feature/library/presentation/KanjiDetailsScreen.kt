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
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.core.ui.theme.Dimens
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
                        .padding(horizontal = Dimens.screenHorizontalPadding),
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
                            ScreenTitleText(details.kanji.literal)
                            MetaText(
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
                                }
                            )
                        }
                    }

                    item {
                        PrimaryButton(
                            onClick = onSaveClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Сохранить кандзи")
                        }
                    }

                    item {
                        LibrarySectionCard(title = "Значения") {
                            if (details.meanings.isEmpty()) {
                                MetaText("Значения пока не указаны.")
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)) {
                                    details.meanings.forEach { meaning ->
                                        Column(verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)) {
                                            BodyText(meaning.meaning)
                                            MetaText(meaning.languageCode.uppercase())
                                            meaning.example?.let {
                                                MetaText(it)
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
                                MetaText("Слова пока не указаны.")
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
        MetaText("Нет данных")
        return
    }

    BodyText(readings.joinToString(" • "))
}
