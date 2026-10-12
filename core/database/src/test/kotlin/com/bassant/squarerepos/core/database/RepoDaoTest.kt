package com.bassant.squarerepos.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.bassant.squarerepos.core.database.model.BookmarkEntity
import com.bassant.squarerepos.core.database.model.RepoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepoDaoTest {

    private lateinit var db: SquareReposDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SquareReposDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun `observeById joins the bookmark flag`() = runTest {
        db.repoDao().upsertAll(listOf(repo(id = 1)))

        db.repoDao().observeById(1).test {
            assertFalse(awaitItem()!!.isBookmarked)

            db.bookmarkDao().insert(BookmarkEntity(repoId = 1, createdAt = 0))
            assertTrue(awaitItem()!!.isBookmarked)

            db.bookmarkDao().delete(1)
            assertFalse(awaitItem()!!.isBookmarked)
        }
    }

    @Test
    fun `clearing the cache keeps bookmarks`() = runTest {
        db.repoDao().upsertAll(listOf(repo(id = 1)))
        db.bookmarkDao().insert(BookmarkEntity(repoId = 1, createdAt = 0))

        db.repoDao().clearAll()
        assertNull(db.repoDao().observeById(1).first())

        // The repository comes back after the next refresh, still bookmarked.
        db.repoDao().upsertAll(listOf(repo(id = 1)))
        assertTrue(db.repoDao().observeById(1).first()!!.isBookmarked)
    }

    @Test
    fun `bookmarking twice keeps a single row`() = runTest {
        db.bookmarkDao().insert(BookmarkEntity(repoId = 1, createdAt = 10))
        db.bookmarkDao().insert(BookmarkEntity(repoId = 1, createdAt = 20))

        assertEquals(listOf(1L), db.bookmarkDao().observeBookmarkedIds().first())
    }

    private fun repo(id: Long) = RepoEntity(
        id = id,
        name = "repo-$id",
        fullName = "square/repo-$id",
        description = null,
        stars = 10,
        forks = 1,
        language = "Kotlin",
        htmlUrl = "https://github.com/square/repo-$id",
        position = id.toInt(),
    )
}
