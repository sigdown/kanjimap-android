package com.vb.kanjimap_android.feature.session.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AuthScreen(
    onLoginCLick: () -> Unit = {}
) {
    Column {
        Text("This is an auth screen")
        Button(onClick = onLoginCLick) {
            Text("Login")
        }
    }
}