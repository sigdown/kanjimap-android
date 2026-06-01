package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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
    onOpenSaved: () -> Unit,
    onWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    ScreenList(
        modifier = modifier.testTag("screen_words"),
        contentPadding = contentPadding
    ) {
        item {
            ScreenHeader(
                title = "Слова",
                subtitle = "Поиск слов, чтений и тематик",
                actions = {
                    IconButton(
                        onClick = onOpenSaved,
                        modifier = Modifier.testTag("open_saved_words_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Save,
                            contentDescription = "Сохранённые слова"
                        )
                    }
                }
            )
        }

        item {
            SurfaceSection(title = "Поиск") {
                LibrarySearchField(
                    value = uiState.query,
                    label = "Найти слово",
                    modifier = Modifier.testTag("words_search_field"),
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
