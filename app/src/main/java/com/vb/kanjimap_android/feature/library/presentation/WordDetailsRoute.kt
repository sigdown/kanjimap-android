package com.vb.kanjimap_android.feature.library.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WordDetailsRoute(
    wordId: Long,
    onKanjiClick: (Long) -> Unit,
    onRelatedWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val viewModel: LibraryViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(wordId) {
        viewModel.loadWordDetails(wordId)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        WordDetailsScreen(
            uiState = uiState.wordDetails,
            onRetry = { viewModel.loadWordDetails(wordId) },
            onSaveClick = viewModel::onSaveWordClick,
            onKanjiClick = onKanjiClick,
            onRelatedWordClick = onRelatedWordClick,
            contentPadding = contentPadding
        )
    }
}
