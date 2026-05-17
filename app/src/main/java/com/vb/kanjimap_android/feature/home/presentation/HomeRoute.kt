package com.vb.kanjimap_android.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    onAuthClick: () -> Unit,
    onOpenReview: () -> Unit,
    onOpenBlocks: () -> Unit,
    onOpenWords: () -> Unit,
    onOpenKanji: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    HomeScreen(
        uiState = uiState,
        onAuthClick = onAuthClick,
        onOpenReview = onOpenReview,
        onOpenBlocks = onOpenBlocks,
        onOpenWords = onOpenWords,
        onOpenKanji = onOpenKanji,
        onRetry = viewModel::loadHome,
        modifier = modifier
    )
}
