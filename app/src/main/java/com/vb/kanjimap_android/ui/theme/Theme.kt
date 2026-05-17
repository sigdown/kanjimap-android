package com.vb.kanjimap_android.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = StudyDarkPrimary,
    onPrimary = StudyDarkOnPrimary,
    primaryContainer = StudyDarkPrimaryContainer,
    onPrimaryContainer = StudyDarkOnPrimaryContainer,
    secondary = StudyDarkSecondary,
    tertiary = StudyDarkTertiary,
    background = StudyDarkBackground,
    onBackground = StudyDarkOnBackground,
    surface = StudyDarkSurface,
    onSurface = StudyDarkOnSurface,
    surfaceVariant = StudyDarkSurfaceVariant,
    onSurfaceVariant = StudyDarkOnSurfaceVariant,
    outline = StudyDarkOutline,
    error = StudyDarkError,
    surfaceContainerLow = StudyDarkSurface,
    surfaceContainer = StudyDarkSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = StudyLightPrimary,
    onPrimary = StudyLightOnPrimary,
    primaryContainer = StudyLightPrimaryContainer,
    onPrimaryContainer = StudyLightOnPrimaryContainer,
    secondary = StudyLightSecondary,
    tertiary = StudyLightTertiary,
    background = StudyLightBackground,
    onBackground = StudyLightOnBackground,
    surface = StudyLightSurface,
    onSurface = StudyLightOnSurface,
    surfaceVariant = StudyLightSurfaceVariant,
    onSurfaceVariant = StudyLightOnSurfaceVariant,
    outline = StudyLightOutline,
    error = StudyLightError,
    surfaceContainerLow = StudyLightSurface,
    surfaceContainer = StudyLightSurfaceVariant
)

@Composable
fun KanjimapandroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
