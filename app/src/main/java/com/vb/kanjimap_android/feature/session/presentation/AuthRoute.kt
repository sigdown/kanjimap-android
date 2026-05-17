package com.vb.kanjimap_android.feature.session.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AuthRoute(
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(uiState.isAuthenticated, uiState.currentUser) {
        if (uiState.isAuthenticated && uiState.currentUser != null) {
            onAuthSuccess()
        }
    }

    AuthScreen(
        uiState = uiState,
        onLogin = viewModel::login,
        onRegister = viewModel::register,
        onSwitchMode = viewModel::setRegisterMode,
        modifier = modifier
    )
}
