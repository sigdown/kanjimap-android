package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StudyRoute(
    blockId: Long,
    mode: StudyMode,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val viewModel: LearningViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(blockId, mode) {
        viewModel.startStudy(blockId, mode)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetStudy()
        }
    }

    StudyScreen(
        uiState = uiState.study,
        onBackClick = onBackClick,
        onShowAnswer = viewModel::showAnswer,
        onPreviousClick = viewModel::previousCard,
        onNextClick = viewModel::nextCard,
        onKnownClick = viewModel::markKnown,
        onUnknownClick = viewModel::markUnknown,
        onRetry = { viewModel.startStudy(blockId, mode) },
        modifier = modifier,
        contentPadding = contentPadding
    )
}
