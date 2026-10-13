package com.bassant.squarerepos.feature.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.bassant.squarerepos.core.designsystem.theme.SquareReposTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `shows name and description`() {
        composeRule.setContent {
            SquareReposTheme {
                DetailsScreen(uiState = DetailsUiState.Success(testRepo()), onBack = {}, onBookmarkClick = {})
            }
        }

        composeRule.onNodeWithText("retrofit").assertIsDisplayed()
        composeRule.onNodeWithText("A type-safe HTTP client").assertIsDisplayed()
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
