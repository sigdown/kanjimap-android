package com.vb.kanjimap_android.feature.session.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AuthRoute(
    sessionViewModel: SessionViewModel,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val uiState = sessionViewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(uiState.isAuthenticated, uiState.currentUser) {
        if (uiState.isAuthenticated && uiState.currentUser != null) {
            onAuthSuccess()
        }
    }

    AuthScreen(
        uiState = uiState,
        onLogin = sessionViewModel::login,
        onRegister = sessionViewModel::register,
        onSwitchMode = sessionViewModel::setRegisterMode,
        modifier = modifier,
        contentPadding = contentPadding
    )
}
