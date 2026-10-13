package com.bassant.squarerepos.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bassant.squarerepos.core.database.model.RepoEntity
import com.bassant.squarerepos.core.database.model.RepoWithBookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {

    /**
     * All cached repositories in API order, each with its bookmark flag.
     * Room invalidates this source when `repos` **or** `bookmarks` change,
     * so the bookmark icon in the list updates automatically.
     */
    @Query(
        """
        SELECT repos.*, EXISTS(SELECT 1 FROM bookmarks WHERE bookmarks.repo_id = repos.id) AS is_bookmarked
        FROM repos
        ORDER BY position ASC
        """,
    )
    fun pagingSource(): PagingSource<Int, RepoWithBookmark>

    /** Observes one repository (and its bookmark flag). Emits `null` if it isn't cached. */
    @Query(
        """
        SELECT repos.*, EXISTS(SELECT 1 FROM bookmarks WHERE bookmarks.repo_id = repos.id) AS is_bookmarked
        FROM repos
        WHERE repos.id = :id
        """,
    )
    fun observeById(id: Long): Flow<RepoWithBookmark?>

    @Upsert
    suspend fun upsertAll(repos: List<RepoEntity>)

    @Query("DELETE FROM repos")
    suspend fun clearAll()
}
