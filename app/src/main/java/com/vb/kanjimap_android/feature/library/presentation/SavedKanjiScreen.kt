package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.feature.library.presentation.components.KanjiListItem
import com.vb.kanjimap_android.feature.library.presentation.components.LibraryEmptyState

@Composable
fun SavedKanjiScreen(
    uiState: SavedKanjiUiState,
    onBackClick: () -> Unit,
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
                title = "Сохранённые кандзи",
                subtitle = "Офлайн-карточки кандзи из Room",
                onBackClick = onBackClick
            )
        }

        if (uiState.items.isEmpty()) {
            item {
                LibraryEmptyState(
                    title = "Пока пусто",
                    description = "Сохраняйте кандзи из карточек, и они появятся здесь.",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
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
