package com.vb.kanjimap_android.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.theme.Dimens

@Composable
fun ErrorView(
    message: String,
    modifier: Modifier = Modifier,
    retryLabel: String = "Retry",
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing, Alignment.CenterVertically)
    ) {
        MetaText(text = message)
        if (onRetry != null) {
            PrimaryButton(onClick = onRetry) {
                androidx.compose.material3.Text(text = retryLabel)
            }
        }
    }
}
