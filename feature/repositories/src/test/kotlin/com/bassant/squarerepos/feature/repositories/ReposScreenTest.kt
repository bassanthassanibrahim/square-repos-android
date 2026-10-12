package com.bassant.squarerepos.feature.repositories

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.bassant.squarerepos.core.designsystem.theme.SquareReposTheme
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
}
