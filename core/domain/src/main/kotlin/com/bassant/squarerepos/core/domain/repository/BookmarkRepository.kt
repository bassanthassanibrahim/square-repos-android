package com.bassant.squarerepos.core.domain.repository

/** Stores the user's bookmarks locally on the device (never sent to GitHub). */
interface BookmarkRepository {

    /** Adds ([bookmarked] = true) or removes ([bookmarked] = false) a bookmark. Safe to call repeatedly. */
    suspend fun setBookmarked(repoId: Long, bookmarked: Boolean)
}
