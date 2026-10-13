package com.bassant.squarerepos.feature.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bassant.squarerepos.core.designsystem.theme.SquareReposTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `shows name, description and bookmark action, and forwards clicks`() {
        var clicks = 0
        composeRule.setContent {
            SquareReposTheme {
                DetailsScreen(
                    uiState = DetailsUiState.Success(testRepo(isBookmarked = false)),
                    onBack = {},
                    onBookmarkClick = { clicks++ },
                )
            }
        }

        composeRule.onNodeWithText("retrofit").assertIsDisplayed()
        composeRule.onNodeWithText("A type-safe HTTP client").assertIsDisplayed()
        composeRule.onNodeWithText("Add to bookmarks").assertIsDisplayed().performClick()
        assertEquals(1, clicks)
    }

    @Test
    fun `bookmarked repository offers removal`() {
        composeRule.setContent {
            SquareReposTheme {
                DetailsScreen(
                    uiState = DetailsUiState.Success(testRepo(isBookmarked = true)),
                    onBack = {},
                    onBookmarkClick = {},
                )
            }
        }

        composeRule.onNodeWithText("Remove from bookmarks").assertIsDisplayed()
    }

    @Test
    fun `missing repository shows not found message`() {
        composeRule.setContent {
            SquareReposTheme {
                DetailsScreen(uiState = DetailsUiState.NotFound, onBack = {}, onBookmarkClick = {})
            }
        }

        composeRule.onNodeWithText("Repository not found").assertIsDisplayed()
    }
}
