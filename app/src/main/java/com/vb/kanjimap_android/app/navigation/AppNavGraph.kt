package com.vb.kanjimap_android.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.PrimaryButton
import com.vb.kanjimap_android.core.ui.components.ScreenTitleText
import com.vb.kanjimap_android.core.ui.components.SecondaryButton
import com.vb.kanjimap_android.core.ui.theme.Dimens
import com.vb.kanjimap_android.feature.home.presentation.HomeRoute
import com.vb.kanjimap_android.feature.learning.presentation.BlockDetailsRoute
import com.vb.kanjimap_android.feature.learning.presentation.LearnRoute
import com.vb.kanjimap_android.feature.learning.presentation.StudyMode
import com.vb.kanjimap_android.feature.learning.presentation.StudyRoute
import com.vb.kanjimap_android.feature.library.presentation.KanjiDetailsRoute
import com.vb.kanjimap_android.feature.library.presentation.KanjiRoute
import com.vb.kanjimap_android.feature.library.presentation.SavedKanjiRoute
import com.vb.kanjimap_android.feature.library.presentation.SavedWordsRoute
import com.vb.kanjimap_android.feature.library.presentation.WordDetailsRoute
import com.vb.kanjimap_android.feature.library.presentation.WordsRoute
import com.vb.kanjimap_android.feature.review.presentation.ReviewRoute
import com.vb.kanjimap_android.feature.session.presentation.AuthRoute
import com.vb.kanjimap_android.feature.session.presentation.SessionViewModel

private const val APP_GRAPH_ROUTE = "app_graph"

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
        route = APP_GRAPH_ROUTE,
        modifier = modifier
    ) {
        composable(Destination.Home.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(APP_GRAPH_ROUTE)
            }
            val sessionViewModel: SessionViewModel = hiltViewModel(parentEntry)
            HomeRoute(
                sessionViewModel = sessionViewModel,
                onAuthClick = { navController.navigate(Destination.Auth.route) },
                onOpenReview = { navController.navigate(Destination.Review.route) },
                onOpenBlocks = { navController.navigateToTopLevel(Destination.Learn.route) },
                onOpenWords = { navController.navigateToTopLevel(Destination.Words.route) },
                onOpenKanji = { navController.navigateToTopLevel(Destination.Kanji.route) },
                contentPadding = innerPadding
            )
        }
        composable(Destination.Learn.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(APP_GRAPH_ROUTE)
            }
            val sessionViewModel: SessionViewModel = hiltViewModel(parentEntry)
            LearnRoute(
                sessionViewModel = sessionViewModel,
                onBlockClick = { blockId ->
                    navController.navigate(Destination.BlockDetails.createRoute(blockId))
                },
                onAuthClick = { navController.navigate(Destination.Auth.route) },
                contentPadding = innerPadding
            )
        }
        composable(Destination.Words.route) {
            WordsRoute(
                onOpenSaved = { navController.navigate(Destination.SavedWords.route) },
                onWordClick = { wordId ->
                    navController.navigate(Destination.WordDetails.createRoute(wordId))
                },
                contentPadding = innerPadding
            )
        }
        composable(Destination.Kanji.route) {
            KanjiRoute(
                onOpenSaved = { navController.navigate(Destination.SavedKanji.route) },
                onKanjiClick = { kanjiId ->
                    navController.navigate(Destination.KanjiDetails.createRoute(kanjiId))
                },
                contentPadding = innerPadding
            )
        }
        composable(Destination.Auth.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(APP_GRAPH_ROUTE)
            }
            val sessionViewModel: SessionViewModel = hiltViewModel(parentEntry)
            AuthRoute(
                sessionViewModel = sessionViewModel,
                onBackClick = { navController.popBackStack() },
                onAuthSuccess = {
                    val returned = navController.popBackStack()
                    if (!returned) {
                        navController.navigate(Destination.Home.route) {
                            popUpTo(Destination.Auth.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                contentPadding = innerPadding
            )
        }
        composable(Destination.Review.route) {
            ReviewRoute(
                onAuthClick = { navController.navigate(Destination.Auth.route) },
                onBackClick = { navController.popBackStack() },
                onClose = { navController.popBackStack() },
                onGoHome = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                contentPadding = innerPadding
            )
        }
        composable(Destination.SavedWords.route) {
            SavedWordsRoute(
                onBackClick = { navController.popBackStack() },
                onWordClick = { wordId ->
                    navController.navigate(Destination.WordDetails.createRoute(wordId))
                },
                contentPadding = innerPadding
            )
        }
        composable(Destination.SavedKanji.route) {
            SavedKanjiRoute(
                onBackClick = { navController.popBackStack() },
                onKanjiClick = { kanjiId ->
                    navController.navigate(Destination.KanjiDetails.createRoute(kanjiId))
                },
                contentPadding = innerPadding
            )
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
                },
                modifier = Modifier.padding(innerPadding)
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
                    onBackClick = { navController.popBackStack() },
                    onKanjiClick = { kanjiId ->
                        navController.navigate(Destination.KanjiDetails.createRoute(kanjiId))
                    },
                    onRelatedWordClick = { relatedWordId ->
                        navController.navigate(Destination.WordDetails.createRoute(relatedWordId))
                    },
                    contentPadding = innerPadding
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
                    onBackClick = { navController.popBackStack() },
                    onWordClick = { wordId ->
                        navController.navigate(Destination.WordDetails.createRoute(wordId))
                    },
                    contentPadding = innerPadding
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
                    onBackClick = { navController.popBackStack() },
                    onStudyClick = { mode ->
                        navController.navigate(Destination.Study.createRoute(blockId, mode.value))
                    },
                    contentPadding = innerPadding
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
                    mode = mode,
                    onBackClick = { navController.popBackStack() },
                    contentPadding = innerPadding
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
        PrimaryButton(onClick = onPrimaryAction, modifier = Modifier.fillMaxWidth()) {
            Text(primaryActionLabel)
        }
        SecondaryButton(onClick = onSecondaryAction, modifier = Modifier.fillMaxWidth()) {
            Text(secondaryActionLabel)
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    description: String,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    ScreenContainer(
        title = title,
        description = description,
        modifier = modifier
    ) {
        if (primaryActionLabel != null && onPrimaryAction != null) {
            PrimaryButton(onClick = onPrimaryAction, modifier = Modifier.fillMaxWidth()) {
                Text(primaryActionLabel)
            }
        }
    }
}

@Composable
private fun ScreenContainer(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.screenContentPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitleText(text = title)
        MetaText(
            text = description,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        content()
    }
}
