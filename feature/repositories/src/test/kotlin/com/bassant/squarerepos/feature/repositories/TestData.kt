package com.bassant.squarerepos.feature.repositories

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.core.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

val okhttp = Repo(
    id = 1,
    name = "okhttp",
    fullName = "square/okhttp",
    description = "Square’s meticulous HTTP client",
    stars = 46_123,
    forks = 9_200,
    language = "Kotlin",
    htmlUrl = "https://github.com/square/okhttp",
    isBookmarked = true,
)

val picasso = okhttp.copy(id = 2, name = "picasso", fullName = "square/picasso", stars = 18_700, isBookmarked = false)

class FakeRepoRepository(private val repos: List<Repo>) : RepoRepository {

    // Explicit load states mark the data as fully loaded; without them `cachedIn` + `asSnapshot` never settle.
    private val loaded = LoadStates(
        refresh = LoadState.NotLoading(endOfPaginationReached = false),
        prepend = LoadState.NotLoading(endOfPaginationReached = true),
        append = LoadState.NotLoading(endOfPaginationReached = true),
    )

    override fun getRepos(): Flow<PagingData<Repo>> = flowOf(PagingData.from(repos, sourceLoadStates = loaded))

    override fun observeRepo(id: Long): Flow<Repo?> = flowOf(repos.firstOrNull { it.id == id })
}
