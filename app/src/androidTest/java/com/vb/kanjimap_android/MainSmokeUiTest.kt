package com.vb.kanjimap_android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class MainSmokeUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appStartsAndShowsHome() {
        composeRule.waitForTag("screen_home")

        composeRule.onNodeWithTag("screen_home")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun bottomNavigationIsVisible() {
        composeRule.waitForTag("bottom_nav_home")

        composeRule.onNodeWithTag("bottom_nav_home").assertExists().assertIsDisplayed()
        composeRule.onNodeWithTag("bottom_nav_learn").assertExists().assertIsDisplayed()
        composeRule.onNodeWithTag("bottom_nav_words").assertExists().assertIsDisplayed()
        composeRule.onNodeWithTag("bottom_nav_kanji").assertExists().assertIsDisplayed()
    }

    @Test
    fun canNavigateBetweenMainScreens() {
        composeRule.waitForTag("bottom_nav_learn")

        composeRule.onNodeWithTag("bottom_nav_learn").performClick()
        composeRule.waitForTag("screen_learn")
        composeRule.onNodeWithTag("screen_learn").assertExists().assertIsDisplayed()

        composeRule.onNodeWithTag("bottom_nav_words").performClick()
        composeRule.waitForTag("screen_words")
        composeRule.onNodeWithTag("screen_words").assertExists().assertIsDisplayed()

        composeRule.onNodeWithTag("bottom_nav_kanji").performClick()
        composeRule.waitForTag("screen_kanji")
        composeRule.onNodeWithTag("screen_kanji").assertExists().assertIsDisplayed()

        composeRule.onNodeWithTag("bottom_nav_home").performClick()
        composeRule.waitForTag("screen_home")
        composeRule.onNodeWithTag("screen_home").assertExists().assertIsDisplayed()
    }

    @Test
    fun wordsSearchFieldAcceptsInput() {
        composeRule.waitForTag("bottom_nav_words")
        composeRule.onNodeWithTag("bottom_nav_words").performClick()
        composeRule.waitForTag("words_search_field")

        composeRule.onNodeWithTag("words_search_field")
            .assertExists()
            .assertIsDisplayed()
            .performTextInput("日")

        composeRule.onNodeWithTag("screen_words")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun kanjiSearchFieldAcceptsInput() {
        composeRule.waitForTag("bottom_nav_kanji")
        composeRule.onNodeWithTag("bottom_nav_kanji").performClick()
        composeRule.waitForTag("kanji_search_field")

        composeRule.onNodeWithTag("kanji_search_field")
            .assertExists()
            .assertIsDisplayed()
            .performTextInput("日")

        composeRule.onNodeWithTag("screen_kanji")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun savedScreensButtonsAreVisible() {
        composeRule.waitForTag("bottom_nav_words")

        composeRule.onNodeWithTag("bottom_nav_words").performClick()
        composeRule.waitForTag("open_saved_words_button")
        composeRule.onNodeWithTag("open_saved_words_button")
            .assertExists()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("bottom_nav_kanji").performClick()
        composeRule.waitForTag("open_saved_kanji_button")
        composeRule.onNodeWithTag("open_saved_kanji_button")
            .assertExists()
            .assertIsDisplayed()
    }

    private fun androidx.compose.ui.test.junit4.AndroidComposeTestRule<*, *>.waitForTag(
        tag: String
    ) {
        waitUntil(timeoutMillis = 5_000) {
            onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
