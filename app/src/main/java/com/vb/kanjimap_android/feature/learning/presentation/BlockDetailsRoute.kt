package com.vb.kanjimap_android.feature.learning.presentation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BlockDetailsRoute(
    blockId: Long,
    onStudyClick: (StudyMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val owner = activityOwner(LocalContext.current)
    val viewModel: LearningViewModel = hiltViewModel(owner)
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
        modifier = modifier
    )
}

private fun activityOwner(context: Context): ComponentActivity = context as ComponentActivity
