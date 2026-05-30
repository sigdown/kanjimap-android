package com.vb.kanjimap_android.feature.review.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReviewRoute(
    onAuthClick: () -> Unit,
    onBackClick: () -> Unit,
    onClose: () -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val viewModel: ReviewViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    ReviewScreen(
        uiState = uiState,
        onAuthClick = onAuthClick,
        onBackClick = onBackClick,
        onClose = onClose,
        onGoHome = onGoHome,
        onRefresh = viewModel::loadReview,
        onRetry = viewModel::loadReview,
        onShowAnswer = viewModel::revealAnswer,
        onAnswerChange = viewModel::updateAnswerInput,
        onSubmitAnswer = viewModel::submitAnswer,
        onNextCard = viewModel::nextCard,
        modifier = modifier,
        contentPadding = contentPadding
    )
}
