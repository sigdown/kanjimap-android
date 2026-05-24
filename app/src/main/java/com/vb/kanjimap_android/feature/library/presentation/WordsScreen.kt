package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.core.ui.components.SurfaceSection
import com.vb.kanjimap_android.feature.library.presentation.components.LibrarySearchField
import com.vb.kanjimap_android.feature.library.presentation.components.WordSuggestionItem

@Composable
fun WordsScreen(
    uiState: WordSearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    ScreenList(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item {
            ScreenHeader(
                title = "Слова",
                subtitle = "Поиск слов, чтений и тематик"
            )
        }

        item {
            SurfaceSection(title = "Поиск") {
                LibrarySearchField(
                    value = uiState.query,
                    label = "Найти слово",
                    onValueChange = onQueryChange,
                    items = uiState.items,
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    emptyMessage = "Ничего не найдено",
                    onSearch = onSearch,
                    onItemClick = { onWordClick(it.wordId) },
                    itemContent = { WordSuggestionItem(it) }
                )
            }
        }
    }
}
