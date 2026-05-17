package com.vb.kanjimap_android.feature.library.presentation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun KanjiDetailsRoute(
    kanjiId: Long,
    onWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: LibraryViewModel = hiltViewModel(activityOwner(LocalContext.current))
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(kanjiId) {
        viewModel.loadKanjiDetails(kanjiId)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        KanjiDetailsScreen(
            uiState = uiState.kanjiDetails,
            onRetry = { viewModel.loadKanjiDetails(kanjiId) },
            onSaveClick = viewModel::onSaveKanjiClick,
            onWordClick = onWordClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private fun activityOwner(context: Context): ComponentActivity = context as ComponentActivity
