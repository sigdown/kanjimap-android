package com.vb.kanjimap_android.feature.session.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.SurfaceSection
import com.vb.kanjimap_android.core.ui.theme.Dimens
import com.vb.kanjimap_android.ui.theme.KanjimapandroidTheme

@Composable
fun AuthScreen(
    uiState: SessionUiState,
    onLogin: (login: String, password: String) -> Unit,
    onRegister: (username: String, email: String, password: String) -> Unit,
    onSwitchMode: (isRegisterMode: Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    var login by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val isRegisterMode = uiState.isRegisterMode
    val credentialLabel = if (isRegisterMode) "Email" else "Логин или email"
    val switchModeText = if (isRegisterMode) {
        "Уже есть аккаунт? Войти"
    } else {
        "Нет аккаунта? Регистрация"
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("screen_auth")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(Dimens.screenContentPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                ScreenHeader(
                    title = if (isRegisterMode) "Регистрация" else "Авторизация",
                    subtitle = if (isRegisterMode) {
                        "Создайте аккаунт, чтобы открыть обучение и повторение"
                    } else {
                        "Войдите, чтобы открыть персональную зону"
                    },
                    onBackClick = onBackClick
                )

                SurfaceSection(title = if (isRegisterMode) "Данные аккаунта" else "Вход") {
                    if (uiState.errorMessage != null) {
                        MetaText(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
                    ) {
                        if (isRegisterMode) {
                            OutlinedTextField(
                                value = username,
                                onValueChange = { username = it },
                                label = { Text("Имя пользователя") },
                                singleLine = true,
                                enabled = !uiState.isLoading,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_username_field")
                            )
                        }

                        OutlinedTextField(
                            value = if (isRegisterMode) email else login,
                            onValueChange = {
                                if (isRegisterMode) email = it else login = it
                            },
                            label = { Text(credentialLabel) },
                            singleLine = true,
                            enabled = !uiState.isLoading,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(if (isRegisterMode) "auth_email_field" else "auth_login_field")
                        )

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_password_field")
                        )

                        PrimaryButton(
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_submit_button")
                        ) {
                            Text(if (isRegisterMode) "Зарегистрироваться" else "Войти")
                        }

                        TextButton(
                            onClick = {
                                onSwitchMode(!isRegisterMode)
                                password = ""
                            },
                            enabled = !uiState.isLoading,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(switchModeText)
                        }
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
            onSwitchMode = {},
            onBackClick = {},
            contentPadding = PaddingValues()
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
            onSwitchMode = {},
            onBackClick = {},
            contentPadding = PaddingValues()
        )
    }
}
