package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vb.kanjimap_android.feature.session.presentation.SessionAuthState
import com.vb.kanjimap_android.feature.session.presentation.SessionViewModel

@Composable
fun LearnRoute(
    sessionViewModel: SessionViewModel,
    onBlockClick: (Long) -> Unit,
    onAuthClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val learningViewModel: LearningViewModel = hiltViewModel()
    val sessionState = sessionViewModel.uiState.collectAsStateWithLifecycle().value
    val uiState = learningViewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(sessionState.authState) {
        if (sessionState.authState == SessionAuthState.AUTHENTICATED) {
            learningViewModel.loadBlocks()
        }
    }

    LearnScreen(
        authState = sessionState.authState,
        uiState = uiState.learn,
        onBlockClick = onBlockClick,
        onAuthClick = onAuthClick,
        onRefresh = { learningViewModel.loadBlocks(force = true) },
        onRetry = { learningViewModel.loadBlocks(force = true) },
        modifier = modifier,
        contentPadding = contentPadding
    )
}
