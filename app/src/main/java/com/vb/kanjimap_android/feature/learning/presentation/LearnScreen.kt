package com.vb.kanjimap_android.feature.learning.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vb.kanjimap_android.core.ui.components.ErrorView
import com.vb.kanjimap_android.core.ui.components.LoadingView
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenTitleText
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.core.ui.theme.Dimens
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningBlockListItem
import com.vb.kanjimap_android.feature.learning.presentation.components.LearningEmptyState

@Composable
fun LearnScreen(
    isAuthenticated: Boolean,
    uiState: LearnUiState,
    onBlockClick: (Long) -> Unit,
    onAuthClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(Dimens.screenContentPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
        ) {
            item {
                ScreenTitleText("Обучение")
            }

            if (!isAuthenticated) {
                item {
                    LearningEmptyState(
                        title = "Войдите, чтобы открыть обучение",
                        description = "После авторизации здесь появятся блоки и режим изучения карточками.",
                        action = {
                            PrimaryButton(onClick = onAuthClick) {
                                Text("Авторизоваться")
                            }
                        }
                    )
                }
                return@LazyColumn
            }

            when {
                uiState.isLoading -> {
                    item {
                        LoadingView(message = "Загружаем блоки")
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
                        LearningEmptyState(
                            title = "Блоков пока нет",
                            description = "Когда они появятся на сервере, вы увидите их здесь."
                        )
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
                    item { Spacer(modifier = Modifier.height(CoreSpacing.xs)) }
                }
            }
        }
    }
}
