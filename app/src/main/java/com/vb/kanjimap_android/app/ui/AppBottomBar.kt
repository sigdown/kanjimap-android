package com.vb.kanjimap_android.app.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vb.kanjimap_android.app.navigation.BottomDestinations
import com.vb.kanjimap_android.app.navigation.navigateToTopLevel

@Composable
fun AppBottomBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        BottomDestinations.items.forEach { destination ->
            val isSelected = currentDestination
                ?.hierarchy
                ?.any { it.route == destination.route } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigateToTopLevel(destination.route)
                },
                modifier = Modifier.testTag(destination.testTag),
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = { Text(destination.label) }
            )
        }
    }
}

private val com.vb.kanjimap_android.app.navigation.BottomDestination.testTag: String
    get() = when (route) {
        com.vb.kanjimap_android.app.navigation.Destination.Home.route -> "bottom_nav_home"
        com.vb.kanjimap_android.app.navigation.Destination.Learn.route -> "bottom_nav_learn"
        com.vb.kanjimap_android.app.navigation.Destination.Words.route -> "bottom_nav_words"
        com.vb.kanjimap_android.app.navigation.Destination.Kanji.route -> "bottom_nav_kanji"
        else -> "bottom_nav_$route"
    }
