package com.bassant.squarerepos.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bassant.squarerepos.core.data.Clock
import com.bassant.squarerepos.core.data.mapper.toDomain
import com.bassant.squarerepos.core.data.paging.SquareReposRemoteMediator
import com.bassant.squarerepos.core.database.SquareReposDatabase
import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.core.domain.repository.RepoRepository
import com.bassant.squarerepos.core.network.GitHubApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Offline-first [RepoRepository]: the network fills Room, the UI only reads Room. */
internal class OfflineFirstRepoRepository @Inject constructor(
    private val api: GitHubApi,
    private val database: SquareReposDatabase,
    private val clock: Clock,
) : RepoRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getRepos(): Flow<PagingData<Repo>> = Pager(
        config = PagingConfig(
            pageSize = SquareReposRemoteMediator.PAGE_SIZE,
            prefetchDistance = SquareReposRemoteMediator.PAGE_SIZE / 3,
            enablePlaceholders = false,
        ),
        remoteMediator = SquareReposRemoteMediator(api = api, database = database, clock = clock),
        pagingSourceFactory = { database.repoDao().pagingSource() },
    ).flow.map { pagingData -> pagingData.map { it.toDomain() } }

    override fun observeRepo(id: Long): Flow<Repo?> =
        database.repoDao().observeById(id)
            .map { it?.toDomain() }
            .distinctUntilChanged()
}
