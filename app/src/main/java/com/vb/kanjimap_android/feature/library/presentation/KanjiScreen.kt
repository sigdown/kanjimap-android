package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.core.ui.components.SurfaceSection
import com.vb.kanjimap_android.feature.library.presentation.components.KanjiSuggestionItem
import com.vb.kanjimap_android.feature.library.presentation.components.LibrarySearchField

@Composable
fun KanjiScreen(
    uiState: KanjiSearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
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
                    items = uiState.items,
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    emptyMessage = "Ничего не найдено",
                    onSearch = onSearch,
                    onItemClick = { onKanjiClick(it.kanjiId) },
                    itemContent = { KanjiSuggestionItem(it) }
                )
            }
        }
    }
}
