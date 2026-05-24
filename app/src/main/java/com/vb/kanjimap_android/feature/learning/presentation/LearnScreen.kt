package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenList
import com.vb.kanjimap_android.core.ui.components.ScreenTitleText
import com.vb.kanjimap_android.core.ui.components.SurfaceSection
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningBlockListItem
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningEmptyState
import com.vb.kanjimap_android.feature.session.presentation.SessionAuthState

@Composable
fun LearnScreen(
    authState: SessionAuthState,
    uiState: LearnUiState,
    onBlockClick: (Long) -> Unit,
    onAuthClick: () -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    if (authState == SessionAuthState.CHECKING) {
        LoadingView(message = "Проверяем сессию")
        return
    }

    if (uiState.isLoading) {
        LoadingView(message = "Загружаем блоки")
        return
    }

    ScreenList(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ScreenTitleText(
                        text = "Обучение",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Обновить"
                        )
                    }
                }
                MetaText("Блоки для первичного изучения слов и кандзи")
            }
        }

        when {
            authState == SessionAuthState.GUEST -> {
                item {
                    SurfaceSection(title = "Доступ к обучению") {
                        LearningEmptyState(
                            title = "Войдите, чтобы открыть обучение",
                            description = "После авторизации здесь появятся блоки и режим изучения карточками.",
                            action = {
                                PrimaryButton(
                                    onClick = onAuthClick,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Авторизоваться")
                                }
                            }
                        )
                    }
                }
            }

            uiState.errorMessage != null -> {
                item {
                    ErrorView(
                        message = uiState.errorMessage,
                        retryLabel = "Повторить",
                        onRetry = onRetry
                    )
                }
            }

            uiState.items.isEmpty() -> {
                item {
                    SurfaceSection(title = "Обучение") {
                        LearningEmptyState(
                            title = "Блоков пока нет",
                            description = "Когда они появятся на сервере, вы увидите их здесь."
                        )
                    }
                }
            }

            else -> {
                items(
                    items = uiState.items,
                    key = { it.learningBlockId }
                ) { block ->
                    LearningBlockListItem(
                        block = block,
                        onClick = { onBlockClick(block.learningBlockId) }
                    )
                }
            }
        }
    }
}
