package com.bassant.squarerepos.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bassant.squarerepos.core.database.dao.BookmarkDao
import com.bassant.squarerepos.core.database.dao.RemoteKeyDao
import com.bassant.squarerepos.core.database.dao.RepoDao
import com.bassant.squarerepos.core.database.model.BookmarkEntity
import com.bassant.squarerepos.core.database.model.RemoteKeyEntity
import com.bassant.squarerepos.core.database.model.RepoEntity

/** The app's single local database: the source of truth for everything shown on screen. */
@Database(
    entities = [
        RepoEntity::class,
        BookmarkEntity::class,
        RemoteKeyEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class SquareReposDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        const val NAME = "square-repos.db"
    }
}
