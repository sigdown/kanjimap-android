package com.vb.kanjimap_android.app.navigation

sealed class Destination(val route: String) {
    data object Home : Destination("home")
    data object Learn : Destination("learn")
    data object Words : Destination("words")
    data object Kanji : Destination("kanji")
    data object Auth : Destination("auth")
    data object Review : Destination("review")
    data object Saved : Destination("saved")
    data object Profile : Destination("profile")
    data object WordDetails : Destination("word_details/{wordId}") {
        fun createRoute(wordId: Long): String = "word_details/$wordId"
    }

    data object KanjiDetails : Destination("kanji_details/{kanjiId}") {
        fun createRoute(kanjiId: Long): String = "kanji_details/$kanjiId"
    }

    data object BlockDetails : Destination("block_details/{blockId}") {
        fun createRoute(blockId: Long): String = "block_details/$blockId"
    }

    data object Study : Destination("study/{blockId}?mode={mode}") {
        fun createRoute(blockId: Long, mode: String): String = "study/$blockId?mode=$mode"
    }
}

object Destinations {
    val bottomBarRoutes: Set<String> = setOf(
        Destination.Home.route,
        Destination.Learn.route,
        Destination.Words.route,
        Destination.Kanji.route
    )

    val allRoutes: List<String> = listOf(
        Destination.Home.route,
        Destination.Learn.route,
        Destination.Words.route,
        Destination.Kanji.route,
        Destination.Auth.route,
        Destination.WordDetails.route,
        Destination.KanjiDetails.route,
        Destination.BlockDetails.route,
        Destination.Study.route,
        Destination.Review.route,
        Destination.Saved.route,
        Destination.Profile.route
    )

    fun shouldShowBottomBar(route: String?): Boolean = route in bottomBarRoutes
}
