package com.vb.kanjimap_android.core.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object Dimens {
    val screenHorizontalPadding = CoreSpacing.lg
    val screenVerticalPadding = CoreSpacing.md
    val sectionSpacing = CoreSpacing.md
    val cardContentPadding = CoreSpacing.md
    val cardInnerSpacing = CoreSpacing.sm
    val cardShape = RoundedCornerShape(20.dp)
    val buttonHeight = 52.dp
    val buttonShape = RoundedCornerShape(16.dp)
    val emptyStateVerticalPadding = 48.dp

    val screenContentPadding = PaddingValues(
        horizontal = screenHorizontalPadding,
        vertical = screenVerticalPadding
    )
}
