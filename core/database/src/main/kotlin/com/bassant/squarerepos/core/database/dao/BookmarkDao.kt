package com.bassant.squarerepos.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bassant.squarerepos.core.database.model.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    /** Adds a bookmark. Bookmarking twice keeps the original date. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE repo_id = :repoId")
    suspend fun delete(repoId: Long)

    @Query("SELECT repo_id FROM bookmarks ORDER BY created_at DESC")
    fun observeBookmarkedIds(): Flow<List<Long>>
}
