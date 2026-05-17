package com.vb.kanjimap_android.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vb.kanjimap_android.feature.home.presentation.HomeRoute
import com.vb.kanjimap_android.feature.learning.presentation.BlockDetailsRoute
import com.vb.kanjimap_android.feature.learning.presentation.LearnRoute
import com.vb.kanjimap_android.feature.learning.presentation.StudyMode
import com.vb.kanjimap_android.feature.learning.presentation.StudyRoute
import com.vb.kanjimap_android.feature.library.presentation.KanjiDetailsRoute
import com.vb.kanjimap_android.feature.library.presentation.KanjiRoute
import com.vb.kanjimap_android.feature.library.presentation.WordDetailsRoute
import com.vb.kanjimap_android.feature.library.presentation.WordsRoute
import com.vb.kanjimap_android.feature.review.presentation.ReviewRoute
import com.vb.kanjimap_android.feature.session.presentation.AuthRoute

@Composable
fun rememberAppNavController(): NavHostController = rememberNavController()

@Composable
fun AppNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(Destination.Home.route) {
            HomeRoute(
                onAuthClick = { navController.navigate(Destination.Auth.route) },
                onOpenReview = { navController.navigate(Destination.Review.route) },
                onOpenBlocks = { navController.navigate(Destination.Learn.route) },
                onOpenWords = { navController.navigate(Destination.Words.route) },
                onOpenKanji = { navController.navigate(Destination.Kanji.route) }
            )
        }
        composable(Destination.Learn.route) {
            LearnRoute(
                onBlockClick = { blockId ->
                    navController.navigate(Destination.BlockDetails.createRoute(blockId))
                },
                onAuthClick = { navController.navigate(Destination.Auth.route) }
            )
        }
        composable(Destination.Words.route) {
            WordsRoute(
                onWordClick = { wordId ->
                    navController.navigate(Destination.WordDetails.createRoute(wordId))
                }
            )
        }
        composable(Destination.Kanji.route) {
            KanjiRoute(
                onKanjiClick = { kanjiId ->
                    navController.navigate(Destination.KanjiDetails.createRoute(kanjiId))
                }
            )
        }
        composable(Destination.Auth.route) {
            AuthRoute(
                onAuthSuccess = {
                    val returned = navController.popBackStack()
                    if (!returned) {
                        navController.navigate(Destination.Home.route) {
                            popUpTo(Destination.Auth.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
        composable(Destination.Review.route) {
            ReviewRoute(
                onAuthClick = { navController.navigate(Destination.Auth.route) },
                onClose = { navController.popBackStack() },
                onGoHome = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Destination.Saved.route) {
            PlaceholderScreen(title = "Saved", description = "Saved content placeholder.")
        }
        composable(Destination.Profile.route) {
            PlaceholderScreen(
                title = "Profile",
                description = "Profile placeholder. Logout flow should be wired at feature level.",
                primaryActionLabel = "Back to Home",
                onPrimaryAction = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Destination.WordDetails.route,
            arguments = listOf(navArgument("wordId") { type = NavType.LongType })
        ) { backStackEntry ->
            val wordId = backStackEntry.arguments?.getLong("wordId")
            if (wordId != null) {
                WordDetailsRoute(
                    wordId = wordId,
                    onKanjiClick = { kanjiId ->
                        navController.navigate(Destination.KanjiDetails.createRoute(kanjiId))
                    },
                    onRelatedWordClick = { relatedWordId ->
                        navController.navigate(Destination.WordDetails.createRoute(relatedWordId))
                    }
                )
            }
        }
        composable(
            route = Destination.KanjiDetails.route,
            arguments = listOf(navArgument("kanjiId") { type = NavType.LongType })
        ) { backStackEntry ->
            val kanjiId = backStackEntry.arguments?.getLong("kanjiId")
            if (kanjiId != null) {
                KanjiDetailsRoute(
                    kanjiId = kanjiId,
                    onWordClick = { wordId ->
                        navController.navigate(Destination.WordDetails.createRoute(wordId))
                    }
                )
            }
        }
        composable(
            route = Destination.BlockDetails.route,
            arguments = listOf(navArgument("blockId") { type = NavType.LongType })
        ) { backStackEntry ->
            val blockId = backStackEntry.arguments?.getLong("blockId")
            if (blockId != null) {
                BlockDetailsRoute(
                    blockId = blockId,
                    onStudyClick = { mode ->
                        navController.navigate(Destination.Study.createRoute(blockId, mode.value))
                    }
                )
            }
        }
        composable(
            route = Destination.Study.route,
            arguments = listOf(
                navArgument("blockId") { type = NavType.LongType },
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = StudyMode.WORDS.value
                }
            )
        ) { backStackEntry ->
            val blockId = backStackEntry.arguments?.getLong("blockId")
            val mode = StudyMode.fromValue(backStackEntry.arguments?.getString("mode"))
            if (blockId != null) {
                StudyRoute(
                    blockId = blockId,
                    mode = mode
                )
            }
        }
    }
}

@Composable
private fun GuestCapableScreen(
    title: String,
    description: String,
    primaryActionLabel: String,
    onPrimaryAction: () -> Unit,
    secondaryActionLabel: String,
    onSecondaryAction: () -> Unit
) {
    ScreenContainer(
        title = title,
        description = description
    ) {
        Button(onClick = onPrimaryAction) {
            Text(primaryActionLabel)
        }
        Button(onClick = onSecondaryAction) {
            Text(secondaryActionLabel)
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    description: String,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null
) {
    ScreenContainer(
        title = title,
        description = description
    ) {
        if (primaryActionLabel != null && onPrimaryAction != null) {
            Button(onClick = onPrimaryAction) {
                Text(primaryActionLabel)
            }
        }
    }
}

@Composable
private fun ScreenContainer(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        content()
    }
}
