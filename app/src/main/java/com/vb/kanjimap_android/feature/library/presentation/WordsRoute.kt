package com.vb.kanjimap_android.feature.library.presentation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WordsRoute(
    onWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: LibraryViewModel = hiltViewModel(activityOwner(LocalContext.current))
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    WordsScreen(
        uiState = uiState.wordSearch,
        onQueryChange = viewModel::updateWordsQuery,
        onSearchClick = viewModel::searchWords,
        onWordClick = onWordClick,
        modifier = modifier
    )
}

private fun activityOwner(context: Context): ComponentActivity = context as ComponentActivity
