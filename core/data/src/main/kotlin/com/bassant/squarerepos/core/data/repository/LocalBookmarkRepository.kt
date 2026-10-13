package com.bassant.squarerepos.core.data.repository

import com.bassant.squarerepos.core.data.Clock
import com.bassant.squarerepos.core.database.dao.BookmarkDao
import com.bassant.squarerepos.core.database.model.BookmarkEntity
import com.bassant.squarerepos.core.domain.repository.BookmarkRepository
import javax.inject.Inject

/** Bookmarks are stored only on the device. */
internal class LocalBookmarkRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    private val clock: Clock,
) : BookmarkRepository {

    override suspend fun setBookmarked(repoId: Long, bookmarked: Boolean) {
        if (bookmarked) {
            bookmarkDao.insert(BookmarkEntity(repoId = repoId, createdAt = clock.nowMillis()))
        } else {
            bookmarkDao.delete(repoId)
        }
    }
}
