package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BlockDetailsRoute(
    blockId: Long,
    onStudyClick: (StudyMode) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val viewModel: LearningViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(blockId) {
        viewModel.loadBlockDetails(blockId)
    }

    BlockDetailsScreen(
        uiState = uiState.blockDetails,
        onRetry = { viewModel.loadBlockDetails(blockId, force = true) },
        onStudyWordsClick = { onStudyClick(StudyMode.WORDS) },
        onStudyKanjiClick = { onStudyClick(StudyMode.KANJI) },
        onStudyAllClick = { onStudyClick(StudyMode.ALL) },
        modifier = modifier,
        contentPadding = contentPadding
    )
}
