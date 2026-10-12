package com.bassant.squarerepos.core.database.di

import android.content.Context
import androidx.room.Room
import com.bassant.squarerepos.core.database.SquareReposDatabase
import com.bassant.squarerepos.core.database.dao.BookmarkDao
import com.bassant.squarerepos.core.database.dao.RemoteKeyDao
import com.bassant.squarerepos.core.database.dao.RepoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SquareReposDatabase =
        Room.databaseBuilder(context, SquareReposDatabase::class.java, SquareReposDatabase.NAME).build()

    @Provides
    fun provideRepoDao(db: SquareReposDatabase): RepoDao = db.repoDao()

    @Provides
    fun provideBookmarkDao(db: SquareReposDatabase): BookmarkDao = db.bookmarkDao()

    @Provides
    fun provideRemoteKeyDao(db: SquareReposDatabase): RemoteKeyDao = db.remoteKeyDao()
}
