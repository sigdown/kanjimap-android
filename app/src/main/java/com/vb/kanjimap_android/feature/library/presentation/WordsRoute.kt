package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WordsRoute(
    onOpenSaved: () -> Unit,
    onWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val viewModel: LibraryViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    WordsScreen(
        uiState = uiState.wordSearch,
        onQueryChange = viewModel::updateWordsQuery,
        onSearch = viewModel::searchWords,
        onOpenSaved = onOpenSaved,
        onWordClick = onWordClick,
        modifier = modifier,
        contentPadding = contentPadding
    )
}
