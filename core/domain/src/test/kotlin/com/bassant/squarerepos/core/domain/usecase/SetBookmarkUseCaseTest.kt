package com.bassant.squarerepos.core.domain.usecase

import com.bassant.squarerepos.core.domain.repository.BookmarkRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SetBookmarkUseCaseTest {

    /** Stores bookmarked ids in memory and records every call. */
    private class FakeBookmarkRepository : BookmarkRepository {
        val bookmarkedIds = mutableSetOf<Long>()
        val calls = mutableListOf<Pair<Long, Boolean>>()

        override suspend fun setBookmarked(repoId: Long, bookmarked: Boolean) {
            calls += repoId to bookmarked
            if (bookmarked) bookmarkedIds += repoId else bookmarkedIds -= repoId
        }
    }

    private val repository = FakeBookmarkRepository()
    private val setBookmark = SetBookmarkUseCase(repository)

    @Test
    fun `passes the target state to the repository`() = runTest {
        setBookmark(repoId = 7, bookmarked = true)
        setBookmark(repoId = 7, bookmarked = false)

        assertEquals(listOf(7L to true, 7L to false), repository.calls)
    }

    @Test
    fun `repeating the same request keeps the same state, unlike a toggle`() = runTest {
        // A fast double tap sends the same target twice; the result must not flip back.
        setBookmark(repoId = 7, bookmarked = true)
        setBookmark(repoId = 7, bookmarked = true)

        assertEquals(setOf(7L), repository.bookmarkedIds)
    }
}
