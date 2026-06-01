package com.vb.kanjimap_android.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.feature.session.presentation.SessionAuthState
import com.vb.kanjimap_android.feature.session.presentation.SessionViewModel

@Composable
fun HomeRoute(
    sessionViewModel: SessionViewModel,
    onAuthClick: () -> Unit,
    onOpenReview: () -> Unit,
    onOpenBlocks: () -> Unit,
    onOpenWords: () -> Unit,
    onOpenKanji: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val sessionState = sessionViewModel.uiState.collectAsStateWithLifecycle().value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(sessionState.authState) {
        when (sessionState.authState) {
            SessionAuthState.AUTHENTICATED -> viewModel.loadHome()
            SessionAuthState.GUEST -> viewModel.clearHome()
            SessionAuthState.CHECKING -> Unit
        }
    }

    if (sessionState.authState == SessionAuthState.CHECKING) {
        LoadingView(
            message = "Проверяем сессию",
            modifier = Modifier.testTag("screen_home")
        )
        return
    }

    HomeScreen(
        uiState = uiState,
        isGuest = sessionState.authState == SessionAuthState.GUEST,
        onAuthClick = onAuthClick,
        onOpenReview = onOpenReview,
        onOpenBlocks = onOpenBlocks,
        onOpenWords = onOpenWords,
        onOpenKanji = onOpenKanji,
        onRetry = { viewModel.loadHome(force = true) },
        onRefresh = { viewModel.loadHome(force = true) },
        modifier = modifier,
        contentPadding = contentPadding
    )
}
