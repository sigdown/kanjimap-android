package com.vb.kanjimap_android.feature.learning.presentation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vb.kanjimap_android.feature.session.presentation.SessionViewModel

@Composable
fun LearnRoute(
    onBlockClick: (Long) -> Unit,
    onAuthClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val owner = activityOwner(LocalContext.current)
    val sessionViewModel: SessionViewModel = hiltViewModel(owner)
    val learningViewModel: LearningViewModel = hiltViewModel(owner)
    val sessionState = sessionViewModel.uiState.collectAsStateWithLifecycle().value
    val uiState = learningViewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(sessionState.isAuthenticated) {
        if (sessionState.isAuthenticated) {
            learningViewModel.loadBlocks()
        }
    }

    LearnScreen(
        isAuthenticated = sessionState.isAuthenticated,
        uiState = uiState.learn,
        onBlockClick = onBlockClick,
        onAuthClick = onAuthClick,
        onRetry = { learningViewModel.loadBlocks(force = true) },
        modifier = modifier
    )
}

private fun activityOwner(context: Context): ComponentActivity = context as ComponentActivity
