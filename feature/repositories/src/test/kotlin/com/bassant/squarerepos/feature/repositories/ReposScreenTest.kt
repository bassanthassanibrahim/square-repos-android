package com.bassant.squarerepos.feature.repositories

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.bassant.squarerepos.core.designsystem.theme.SquareReposTheme
import com.bassant.squarerepos.core.domain.error.DataError
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ReposScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `shows name and stars, and opens details on click`() {
        var openedId: Long? = null
        composeRule.setContent {
            SquareReposTheme {
                ReposScreen(
                    repos = flowOf(PagingData.from(listOf(okhttp, picasso))).collectAsLazyPagingItems(),
                    onRepoClick = { openedId = it },
                )
            }
        }

        composeRule.onNodeWithText("okhttp").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("46123 stars").assertIsDisplayed()

        composeRule.onNodeWithText("picasso").performClick()
        assertEquals(2L, openedId)
    }

    @Test
    fun `failed refresh with saved data says the device is offline`() {
        showSavedDataAfterRefreshError(DataError.NoConnection())

        composeRule.onNodeWithText("You're offline. Showing saved repositories.").assertIsDisplayed()
        composeRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun `failed refresh with saved data explains the rate limit`() {
        showSavedDataAfterRefreshError(DataError.RateLimited(resetAtEpochSeconds = null))

        composeRule.onNodeWithText("GitHub's request limit was reached. Showing saved repositories.").assertIsDisplayed()
    }

    @Test
    fun `failed refresh with saved data falls back to a generic message`() {
        showSavedDataAfterRefreshError(DataError.Server(code = 500))

        composeRule.onNodeWithText("Couldn't refresh. Showing saved repositories.").assertIsDisplayed()
    }

    /** Saved repositories are on screen, but the network refresh failed with [error]. */
    private fun showSavedDataAfterRefreshError(error: Throwable) {
        val notLoading = LoadState.NotLoading(endOfPaginationReached = true)
        val pagingData = PagingData.from(
            data = listOf(okhttp, picasso),
            sourceLoadStates = LoadStates(LoadState.NotLoading(false), notLoading, notLoading),
            mediatorLoadStates = LoadStates(LoadState.Error(error), notLoading, notLoading),
        )
        composeRule.setContent {
            SquareReposTheme {
                ReposScreen(repos = flowOf(pagingData).collectAsLazyPagingItems(), onRepoClick = {})
            }
        }
    }
}
