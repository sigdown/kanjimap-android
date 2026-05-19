package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenHeader
import com.vb.kanjimap_android.core.ui.components.ScreenList
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
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    ScreenList(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item {
            ScreenHeader(
                title = "Обучение",
                subtitle = "Блоки для первичного изучения слов и кандзи"
            )
        }

        when {
            authState == SessionAuthState.CHECKING -> {
                item { LoadingView(message = "Проверяем сессию") }
            }

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

            uiState.isLoading -> {
                item { LoadingView(message = "Загружаем блоки") }
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
