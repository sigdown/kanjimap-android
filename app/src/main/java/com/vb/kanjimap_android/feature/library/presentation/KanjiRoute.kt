package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun KanjiRoute(
    onKanjiClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val viewModel: LibraryViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    KanjiScreen(
        uiState = uiState.kanjiSearch,
        onQueryChange = viewModel::updateKanjiQuery,
        onSearchClick = viewModel::searchKanji,
        onKanjiClick = onKanjiClick,
        modifier = modifier,
        contentPadding = contentPadding
    )
}
