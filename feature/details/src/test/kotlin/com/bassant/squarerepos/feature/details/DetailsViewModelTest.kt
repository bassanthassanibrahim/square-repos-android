package com.bassant.squarerepos.feature.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.bassant.squarerepos.core.domain.usecase.ObserveRepoUseCase
import com.bassant.squarerepos.feature.details.navigation.DetailsDestination
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val store = FakeStore(listOf(testRepo(id = 1)))

    private fun viewModel(repoId: Long = 1) = DetailsViewModel(
        savedStateHandle = SavedStateHandle(mapOf(DetailsDestination.ARG_REPO_ID to repoId)),
        observeRepo = ObserveRepoUseCase(FakeRepoRepository(store)),
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

    /** Skips the initial `Loading` value, whose delivery depends on subscription timing. */
    private suspend fun ReceiveTurbine<DetailsUiState>.awaitLoaded(): DetailsUiState {
        var item = awaitItem()
        while (item == DetailsUiState.Loading) item = awaitItem()
        return item
    }
}
