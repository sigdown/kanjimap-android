package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.feature.library.presentation.components.LibraryEmptyState
import com.vb.kanjimap_android.feature.library.presentation.components.LibrarySearchField
import com.vb.kanjimap_android.feature.library.presentation.components.WordListItem

@Composable
fun WordsScreen(
    uiState: WordSearchUiState,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = CoreSpacing.lg, vertical = CoreSpacing.md),
            verticalArrangement = Arrangement.spacedBy(CoreSpacing.md)
        ) {
            item {
                Text(
                    text = "Слова",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                LibrarySearchField(
                    value = uiState.query,
                    label = "Найти слово",
                    onValueChange = onQueryChange,
                    onSearch = onSearchClick,
                    enabled = !uiState.isLoading
                )
            }

            when {
                uiState.isLoading -> {
                    item {
                        LoadingView(message = "Ищем слова")
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
                            description = "Введите слово, чтение или тему, чтобы увидеть результаты."
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
                        key = { it.wordId }
                    ) { word ->
                        WordListItem(
                            word = word,
                            onClick = { onWordClick(word.wordId) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(CoreSpacing.xs)) }
                }
            }
        }
    }
}
