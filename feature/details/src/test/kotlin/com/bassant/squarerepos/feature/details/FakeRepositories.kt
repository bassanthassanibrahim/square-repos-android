package com.bassant.squarerepos.feature.details

import androidx.paging.PagingData
import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.core.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

/** In-memory "database" observed reactively, like Room. */
class FakeStore(repos: List<Repo>) {
    val repos = MutableStateFlow(repos.associateBy { it.id })
}

class FakeRepoRepository(private val store: FakeStore) : RepoRepository {
    override fun getRepos(): Flow<PagingData<Repo>> = emptyFlow()
    override fun observeRepo(id: Long): Flow<Repo?> = store.repos.map { it[id] }
}

fun testRepo(id: Long = 1, isBookmarked: Boolean = false) = Repo(
    id = id,
    name = "retrofit",
    fullName = "square/retrofit",
    description = "A type-safe HTTP client",
    stars = 43_100,
    forks = 7_300,
    language = "Kotlin",
    htmlUrl = "https://github.com/square/retrofit",
    isBookmarked = isBookmarked,
)
