package com.bassant.squarerepos.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A repository the user bookmarked.
 *
 * Bookmarks live in their **own table** on purpose: the repository cache is cleared on every
 * refresh, and keeping bookmarks separate guarantees a refresh can never delete them.
 */
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey @ColumnInfo(name = "repo_id") val repoId: Long,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
