package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.core.ui.components.SurfaceSection
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
    ScreenList(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item {
            ScreenHeader(
                title = "Кандзи",
                subtitle = "Поиск символов и базовой информации"
            )
        }

        item {
            SurfaceSection(title = "Поиск") {
                LibrarySearchField(
                    value = uiState.query,
                    label = "Найти кандзи",
                    onValueChange = onQueryChange,
                    onSearch = onSearchClick,
                    enabled = !uiState.isLoading
                )
            }
        }

        when {
            uiState.isLoading -> {
                item { LoadingView(message = "Ищем кандзи") }
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
                    SurfaceSection(title = "Результаты") {
                        LibraryEmptyState(
                            title = "Начните поиск",
                            description = "Введите символ или часть запроса, чтобы увидеть кандзи."
                        )
                    }
                }
            }

            uiState.items.isEmpty() -> {
                item {
                    SurfaceSection(title = "Результаты") {
                        LibraryEmptyState(
                            title = "Ничего не найдено",
                            description = "Попробуйте другой запрос."
                        )
                    }
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
            }
        }
    }
}
