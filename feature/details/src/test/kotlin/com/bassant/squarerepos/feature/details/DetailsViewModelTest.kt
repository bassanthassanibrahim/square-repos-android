package com.bassant.squarerepos.feature.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.bassant.squarerepos.core.domain.usecase.ObserveRepoUseCase
import com.bassant.squarerepos.core.domain.usecase.SetBookmarkUseCase
import com.bassant.squarerepos.feature.details.navigation.DetailsDestination
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val store = FakeStore(listOf(testRepo(id = 1)))
    private val bookmarkRepository = FakeBookmarkRepository(store)

    private fun viewModel(repoId: Long = 1) = DetailsViewModel(
        savedStateHandle = SavedStateHandle(mapOf(DetailsDestination.ARG_REPO_ID to repoId)),
        observeRepo = ObserveRepoUseCase(FakeRepoRepository(store)),
        setBookmark = SetBookmarkUseCase(bookmarkRepository),
    )

    @Test
    fun `shows the repository read from the data layer`() = runTest {
        viewModel().uiState.test {
            assertEquals(DetailsUiState.Success(testRepo(id = 1)), awaitLoaded())
        }
    }

    @Test
    fun `unknown id shows NotFound`() = runTest {
        viewModel(repoId = 99).uiState.test {
            assertEquals(DetailsUiState.NotFound, awaitLoaded())
        }
    }

    @Test
    fun `updates reactively when the stored repository changes`() = runTest {
        viewModel().uiState.test {
            assertEquals(43_100, (awaitLoaded() as DetailsUiState.Success).repo.stars)

            store.repos.value = mapOf(1L to testRepo(id = 1).copy(stars = 50_000))
            assertEquals(50_000, (awaitItem() as DetailsUiState.Success).repo.stars)
        }
    }

    @Test
    fun `bookmark click adds then removes the bookmark reactively`() = runTest {
        val viewModel = viewModel()
        viewModel.uiState.test {
            assertFalse((awaitLoaded() as DetailsUiState.Success).repo.isBookmarked)

            viewModel.onBookmarkClick()
            assertTrue((awaitItem() as DetailsUiState.Success).repo.isBookmarked)

            viewModel.onBookmarkClick()
            assertFalse((awaitItem() as DetailsUiState.Success).repo.isBookmarked)
        }
        assertEquals(2, bookmarkRepository.calls)
    }

    @Test
    fun `bookmark click is ignored while loading or not found`() = runTest {
        val viewModel = viewModel(repoId = 99)
        viewModel.uiState.test {
            assertEquals(DetailsUiState.NotFound, awaitLoaded())
            viewModel.onBookmarkClick()
        }
        assertEquals(0, bookmarkRepository.calls)
    }

    /** Skips the initial `Loading` value, whose delivery depends on subscription timing. */
    private suspend fun ReceiveTurbine<DetailsUiState>.awaitLoaded(): DetailsUiState {
        var item = awaitItem()
        while (item == DetailsUiState.Loading) item = awaitItem()
        return item
    }
}
