package com.vb.kanjimap_android.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
    val lifecycleOwner = LocalLifecycleOwner.current
    val sessionState = sessionViewModel.uiState.collectAsStateWithLifecycle().value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(sessionState.authState) {
        if (sessionState.authState == SessionAuthState.AUTHENTICATED) {
            viewModel.loadHome()
        }
    }

    DisposableEffect(lifecycleOwner, viewModel, sessionState.authState) {
        val observer = LifecycleEventObserver { _, event ->
            if (
                event == Lifecycle.Event.ON_RESUME &&
                sessionState.authState == SessionAuthState.AUTHENTICATED
            ) {
                viewModel.loadHome()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (sessionState.authState == SessionAuthState.CHECKING) {
        LoadingView(message = "Проверяем сессию")
        return
    }

    HomeScreen(
        uiState = uiState,
        onAuthClick = onAuthClick,
        onOpenReview = onOpenReview,
        onOpenBlocks = onOpenBlocks,
        onOpenWords = onOpenWords,
        onOpenKanji = onOpenKanji,
        onRetry = viewModel::loadHome,
        modifier = modifier,
        contentPadding = contentPadding
    )
}
