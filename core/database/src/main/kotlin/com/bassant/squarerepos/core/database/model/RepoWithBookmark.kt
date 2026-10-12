package com.bassant.squarerepos.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

/** A cached repository together with its bookmark state, produced by a single SQL query. */
data class RepoWithBookmark(
    @Embedded val repo: RepoEntity,
    @ColumnInfo(name = "is_bookmarked") val isBookmarked: Boolean,
)
