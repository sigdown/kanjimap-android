package com.vb.kanjimap_android.app.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vb.kanjimap_android.app.navigation.AppNavGraph
import com.vb.kanjimap_android.app.navigation.Destinations
import com.vb.kanjimap_android.app.navigation.rememberAppNavController

@Composable
fun AppScaffold() {
    val navController = rememberAppNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = Destinations.shouldShowBottomBar(currentRoute)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}
