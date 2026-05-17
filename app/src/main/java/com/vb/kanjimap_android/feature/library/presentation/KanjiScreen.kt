package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.core.ui.components.ScreenTitleText
import com.vb.kanjimap_android.core.ui.theme.Dimens
import com.vb.kanjimap_android.feature.library.presentation.components.KanjiListItem
import com.vb.kanjimap_android.feature.library.presentation.components.LibraryEmptyState
import com.vb.kanjimap_android.feature.library.presentation.components.LibrarySearchField

@Composable
fun KanjiScreen(
    uiState: KanjiSearchUiState,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onKanjiClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(contentPadding)
                .padding(Dimens.screenContentPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
        ) {
            item {
                ScreenTitleText("Кандзи")
            }

            item {
                LibrarySearchField(
                    value = uiState.query,
                    label = "Найти кандзи",
                    onValueChange = onQueryChange,
                    onSearch = onSearchClick,
                    enabled = !uiState.isLoading
                )
            }

            when {
                uiState.isLoading -> {
                    item {
                        LoadingView(message = "Ищем кандзи")
                    }
                }

                uiState.errorMessage != null -> {
                    item {
                        ErrorView(
                            message = uiState.errorMessage,
                            retryLabel = "Повторить",
                            onRetry = onSearchClick
                        )
                    }
                }

                !uiState.hasSearched -> {
                    item {
                        LibraryEmptyState(
                            title = "Начните поиск",
                            description = "Введите символ или часть запроса, чтобы увидеть кандзи."
                        )
                    }
                }

                uiState.items.isEmpty() -> {
                    item {
                        LibraryEmptyState(
                            title = "Ничего не найдено",
                            description = "Попробуйте другой запрос."
                        )
                    }
                }

                else -> {
                    items(
                        items = uiState.items,
                        key = { it.kanjiId }
                    ) { kanji ->
                        KanjiListItem(
                            kanji = kanji,
                            onClick = { onKanjiClick(kanji.kanjiId) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(CoreSpacing.xs)) }
                }
            }
        }
    }
}
