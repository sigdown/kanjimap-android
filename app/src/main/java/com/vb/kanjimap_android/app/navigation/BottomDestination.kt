package com.vb.kanjimap_android.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

object BottomDestinations {
    val items: List<BottomDestination> = listOf(
        BottomDestination(
            route = Destination.Home.route,
            label = "Home",
            icon = Icons.Filled.Home
        ),
        BottomDestination(
            route = Destination.Learn.route,
            label = "Learn",
            icon = Icons.Filled.School
        ),
        BottomDestination(
            route = Destination.Words.route,
            label = "Words",
            icon = Icons.AutoMirrored.Filled.MenuBook
        ),
        BottomDestination(
            route = Destination.Kanji.route,
            label = "Kanji",
            icon = Icons.Filled.Translate
        )
    )
}
