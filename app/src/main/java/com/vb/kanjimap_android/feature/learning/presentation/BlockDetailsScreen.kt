package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.BodyText
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.ScreenTitleText
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.core.ui.theme.Dimens
import com.vb.kanjimap_android.feature.learning.presentation.components.KanjiChips
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningEmptyState
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningSectionCard
import com.vb.kanjimap_android.feature.learning.presentation.components.StudyModeButtons
import com.vb.kanjimap_android.feature.learning.presentation.components.WordsChips

@Composable
fun BlockDetailsScreen(
    uiState: BlockDetailsUiState,
    onRetry: () -> Unit,
    onStudyWordsClick: () -> Unit,
    onStudyKanjiClick: () -> Unit,
    onStudyAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingView(message = "Загружаем блок")
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
                LearningEmptyState(
                    title = "Нет данных по блоку",
                    description = "Попробуйте открыть блок позже.",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimens.screenHorizontalPadding)
                )
            }

            else -> {
                val details = uiState.item
                val canStudyWords = details.words.isNotEmpty()
                val canStudyKanji = details.kanjis.isNotEmpty()
                val canStudyAll = canStudyWords && canStudyKanji

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(Dimens.screenContentPadding),
                    verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
                ) {
                    item {
                        ScreenTitleText(details.block.title)
                    }

                    item {
                        LearningSectionCard(title = "О блоке") {
                            details.block.description?.let {
                                BodyText(it)
                            }
                            MetaText("Тип: ${details.block.blockType}")
                        }
                    }

                    item {
                        StudyModeButtons(
                            showWords = canStudyWords,
                            showKanji = canStudyKanji,
                            showAll = canStudyAll,
                            onWordsClick = onStudyWordsClick,
                            onKanjiClick = onStudyKanjiClick,
                            onAllClick = onStudyAllClick
                        )
                    }

                    item {
                        LearningSectionCard(title = "Слова") {
                            if (details.words.isEmpty()) {
                                MetaText("Слова в этом блоке пока не добавлены.")
                            } else {
                                WordsChips(words = details.words)
                            }
                        }
                    }

                    item {
                        LearningSectionCard(title = "Кандзи") {
                            if (details.kanjis.isEmpty()) {
                                MetaText("Кандзи в этом блоке пока не добавлены.")
                            } else {
                                KanjiChips(kanjis = details.kanjis)
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(CoreSpacing.xs)) }
                }
            }
        }
    }
}
