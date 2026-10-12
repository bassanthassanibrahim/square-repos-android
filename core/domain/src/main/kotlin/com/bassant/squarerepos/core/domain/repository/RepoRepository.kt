package com.bassant.squarerepos.core.domain.repository

import androidx.paging.PagingData
import com.bassant.squarerepos.core.domain.model.Repo
import kotlinx.coroutines.flow.Flow

/**
 * Source of truth for Square's repositories.
 *
 * Implementations fetch pages from GitHub, store them in the local database and
 * always serve data **from the database**, so the app keeps working offline.
 */
interface RepoRepository {

    /** Paged, database-backed stream of repositories. Loads the next page from the network on demand. */
    fun getRepos(): Flow<PagingData<Repo>>

    /** Observes a single repository. Emits `null` if it is not stored locally. */
    fun observeRepo(id: Long): Flow<Repo?>
}
