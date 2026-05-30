package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.feature.library.presentation.components.LibraryEmptyState
import com.vb.kanjimap_android.feature.library.presentation.components.WordListItem

@Composable
fun SavedWordsScreen(
    uiState: SavedWordsUiState,
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
                title = "Сохранённые слова",
                subtitle = "Офлайн-карточки слов из Room"
            )
        }

        if (uiState.items.isEmpty()) {
            item {
                LibraryEmptyState(
                    title = "Пока пусто",
                    description = "Сохраняйте слова из карточек, и они появятся здесь.",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            items(
                items = uiState.items,
                key = { it.wordId }
            ) { word ->
                WordListItem(
                    word = word,
                    onClick = { onWordClick(word.wordId) }
                )
            }
        }
    }
}
