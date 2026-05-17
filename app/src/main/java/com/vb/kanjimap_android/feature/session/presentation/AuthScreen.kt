package com.vb.kanjimap_android.feature.session.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.ui.theme.KanjimapandroidTheme

@Composable
fun AuthScreen(
    uiState: SessionUiState,
    onLogin: (login: String, password: String) -> Unit,
    onRegister: (username: String, email: String, password: String) -> Unit,
    onSwitchMode: (isRegisterMode: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var login by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val isRegisterMode = uiState.isRegisterMode
    val scrollState = rememberScrollState()
    val credentialLabel = if (isRegisterMode) "Email" else "Логин или email"
    val switchModeText = if (isRegisterMode) {
        "Уже есть аккаунт? Войти"
    } else {
        "Нет аккаунта? Регистрация"
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(scrollState)
                    .padding(horizontal = CoreSpacing.lg, vertical = CoreSpacing.md)
            ) {
                Text(
                    text = if (isRegisterMode) "Регистрация" else "Авторизация",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(CoreSpacing.md))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (uiState.errorMessage != null) {
                        ErrorMessage(message = uiState.errorMessage)
                        Spacer(modifier = Modifier.height(CoreSpacing.md))
                    }

                    if (isRegisterMode) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Имя пользователя") },
                            singleLine = true,
                            enabled = !uiState.isLoading,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(CoreSpacing.md))
                    }

                    OutlinedTextField(
                        value = if (isRegisterMode) email else login,
                        onValueChange = {
                            if (isRegisterMode) {
                                email = it
                            } else {
                                login = it
                            }
                        },
                        label = { Text(credentialLabel) },
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(CoreSpacing.md))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Пароль") },
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(CoreSpacing.md))

                    Button(
                        onClick = {
                            if (isRegisterMode) {
                                onRegister(username.trim(), email.trim(), password)
                            } else {
                                onLogin(login.trim(), password)
                            }
                        },
                        enabled = !uiState.isLoading && canSubmit(
                            isRegisterMode = isRegisterMode,
                            login = login,
                            username = username,
                            email = email,
                            password = password
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isRegisterMode) "Зарегистрироваться" else "Войти")
                    }

                    Spacer(modifier = Modifier.height(CoreSpacing.sm))

                    TextButton(
                        onClick = {
                            onSwitchMode(!isRegisterMode)
                            password = ""
                        },
                        enabled = !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = switchModeText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium
    )
}

private fun canSubmit(
    isRegisterMode: Boolean,
    login: String,
    username: String,
    email: String,
    password: String
): Boolean {
    if (password.isBlank()) return false
    return if (isRegisterMode) {
        username.isNotBlank() && email.isNotBlank()
    } else {
        login.isNotBlank()
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    KanjimapandroidTheme {
        AuthScreen(
            uiState = SessionUiState(
                errorMessage = "Неверный логин или пароль"
            ),
            onLogin = { _, _ -> },
            onRegister = { _, _, _ -> },
            onSwitchMode = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenRegisterPreview() {
    KanjimapandroidTheme {
        AuthScreen(
            uiState = SessionUiState(
                isRegisterMode = true
            ),
            onLogin = { _, _ -> },
            onRegister = { _, _, _ -> },
            onSwitchMode = {}
        )
    }
}
