package com.bassant.squarerepos.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.bassant.squarerepos.core.data.paging.SquareReposRemoteMediator
import com.bassant.squarerepos.core.database.SquareReposDatabase
import com.bassant.squarerepos.core.database.model.BookmarkEntity
import com.bassant.squarerepos.core.database.model.RepoWithBookmark
import com.bassant.squarerepos.core.domain.error.DataError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
class SquareReposRemoteMediatorTest {

    private lateinit var db: SquareReposDatabase
    private var now = 1_000_000L
    private val clock = Clock { now }
    private val pageSize = 10
    private val emptyState = PagingState<Int, RepoWithBookmark>(
        pages = emptyList(),
        anchorPosition = null,
        config = PagingConfig(pageSize),
        leadingPlaceholderCount = 0,
    )

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SquareReposDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() = db.close()

    private fun mediator(api: FakeGitHubApi) =
        SquareReposRemoteMediator(api = api, database = db, clock = clock, pageSize = pageSize)

    @Test
    fun `refresh stores the first page and the next page key`() = runTest {
        val result = mediator(FakeGitHubApi(totalRepos = 25)).load(LoadType.REFRESH, emptyState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        assertNotNull(db.repoDao().observeById(10).first())
        assertEquals(2, db.remoteKeyDao().get("square")?.nextPage)
    }

    @Test
    fun `append loads following pages until the end`() = runTest {
        val api = FakeGitHubApi(totalRepos = 25)
        val mediator = mediator(api)

        mediator.load(LoadType.REFRESH, emptyState)
        mediator.load(LoadType.APPEND, emptyState)
        val last = mediator.load(LoadType.APPEND, emptyState) as RemoteMediator.MediatorResult.Success

        assertEquals(listOf(1, 2, 3), api.requestedPages)
        assertTrue(last.endOfPaginationReached)
        assertNull(db.remoteKeyDao().get("square")?.nextPage)
        assertNotNull(db.repoDao().observeById(25).first())
    }

    @Test
    fun `refresh keeps bookmarks`() = runTest {
        val mediator = mediator(FakeGitHubApi(totalRepos = 5))
        mediator.load(LoadType.REFRESH, emptyState)
        db.bookmarkDao().insert(BookmarkEntity(repoId = 3, createdAt = 0))

        mediator.load(LoadType.REFRESH, emptyState)

        assertTrue(db.repoDao().observeById(3).first()!!.isBookmarked)
    }

    @Test
    fun `network failure is reported as NoConnection and keeps the cache`() = runTest {
        val api = FakeGitHubApi(totalRepos = 5)
        val mediator = mediator(api)
        mediator.load(LoadType.REFRESH, emptyState)

        api.failure = IOException("offline")
        val result = mediator.load(LoadType.REFRESH, emptyState)

        assertTrue((result as RemoteMediator.MediatorResult.Error).throwable is DataError.NoConnection)
        assertNotNull(db.repoDao().observeById(1).first())
    }

    @Test
    fun `fresh cache skips the initial refresh, stale cache refreshes`() = runTest {
        val mediator = mediator(FakeGitHubApi(totalRepos = 5))
        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, mediator.initialize())

        mediator.load(LoadType.REFRESH, emptyState)
        assertEquals(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH, mediator.initialize())

        now += 2 * 60 * 60 * 1000L // two hours later
        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, mediator.initialize())
    }
}
