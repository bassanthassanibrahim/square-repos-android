package com.bassant.squarerepos.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bassant.squarerepos.core.data.Clock
import com.bassant.squarerepos.core.data.mapper.toDataError
import com.bassant.squarerepos.core.data.mapper.toEntity
import com.bassant.squarerepos.core.database.SquareReposDatabase
import com.bassant.squarerepos.core.database.model.RemoteKeyEntity
import com.bassant.squarerepos.core.database.model.RepoWithBookmark
import com.bassant.squarerepos.core.network.GitHubApi
import kotlinx.coroutines.CancellationException
import java.util.concurrent.TimeUnit

/**
 * Loads pages from GitHub and writes them into Room.
 *
 * The UI **never** reads from the network directly: it observes the database through a
 * `PagingSource`, and this mediator only fills the database (single source of truth).
 *
 * - **REFRESH**: loads page 1 and replaces the cache (bookmarks are in another table, so they survive).
 * - **APPEND**: loads the next page stored in [RemoteKeyEntity].
 * - **PREPEND**: never needed, the list only grows downwards.
 *
 * On start, a cache younger than [cacheTimeoutMillis] is shown without hitting the network,
 * which also protects GitHub's rate limit.
 */
@OptIn(ExperimentalPagingApi::class)
class SquareReposRemoteMediator(
    private val api: GitHubApi,
    private val database: SquareReposDatabase,
    private val clock: Clock,
    private val org: String = ORG,
    private val pageSize: Int = PAGE_SIZE,
    private val cacheTimeoutMillis: Long = TimeUnit.HOURS.toMillis(1),
) : RemoteMediator<Int, RepoWithBookmark>() {

    private val repoDao = database.repoDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val lastUpdated = remoteKeyDao.get(org)?.lastUpdated ?: return InitializeAction.LAUNCH_INITIAL_REFRESH
        val isCacheFresh = clock.nowMillis() - lastUpdated < cacheTimeoutMillis
        return if (isCacheFresh) InitializeAction.SKIP_INITIAL_REFRESH else InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, RepoWithBookmark>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> FIRST_PAGE
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                // A missing key means no refresh succeeded yet; a null nextPage means we reached the end.
                val nextPage = remoteKeyDao.get(org)?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                nextPage
            }
        }

        return try {
            val repos = api.getOrgRepos(org = org, page = page, perPage = pageSize)
            // GitHub returns fewer items than requested on the last page.
            val endOfPaginationReached = repos.size < pageSize

            database.withTransaction {
                val previousKey = remoteKeyDao.get(org)
                if (loadType == LoadType.REFRESH) {
                    repoDao.clearAll()
                }
                val firstPosition = (page - 1) * pageSize
                repoDao.upsertAll(repos.mapIndexed { index, repo -> repo.toEntity(position = firstPosition + index) })
                remoteKeyDao.upsert(
                    RemoteKeyEntity(
                        label = org,
                        nextPage = if (endOfPaginationReached) null else page + 1,
                        lastUpdated = if (loadType == LoadType.REFRESH || previousKey == null) {
                            clock.nowMillis()
                        } else {
                            previousKey.lastUpdated
                        },
                    ),
                )
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            MediatorResult.Error(e.toDataError())
        }
    }

    companion object {
        const val ORG = "square"
        const val PAGE_SIZE = 30
        private const val FIRST_PAGE = 1
    }
}
