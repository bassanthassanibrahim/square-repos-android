package com.bassant.squarerepos.core.data.di

import com.bassant.squarerepos.core.data.Clock
import com.bassant.squarerepos.core.data.repository.OfflineFirstRepoRepository
import com.bassant.squarerepos.core.domain.repository.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindRepoRepository(impl: OfflineFirstRepoRepository): RepoRepository

    companion object {
        @Provides
        fun provideClock(): Clock = Clock { System.currentTimeMillis() }
    }
}
