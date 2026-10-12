package com.bassant.squarerepos.feature.repositories

import androidx.paging.testing.asSnapshot
import com.bassant.squarerepos.core.domain.usecase.GetReposUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ReposViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `exposes the repositories from the domain layer`() = runTest {
        val viewModel = ReposViewModel(GetReposUseCase(FakeRepoRepository(listOf(okhttp, picasso))))

        assertEquals(listOf(okhttp, picasso), viewModel.repos.asSnapshot())
    }
}
