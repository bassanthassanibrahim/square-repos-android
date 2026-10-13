package com.bassant.squarerepos.feature.details

import androidx.paging.PagingData
import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.core.domain.repository.BookmarkRepository
import com.bassant.squarerepos.core.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * One in-memory "database" shared by both fake repositories, so a bookmark change is
 * observed reactively, exactly like with Room.
 */
class FakeStore(repos: List<Repo>) {
    val repos = MutableStateFlow(repos.associateBy { it.id })
}

class FakeRepoRepository(private val store: FakeStore) : RepoRepository {
    override fun getRepos(): Flow<PagingData<Repo>> = emptyFlow()
    override fun observeRepo(id: Long): Flow<Repo?> = store.repos.map { it[id] }
}

class FakeBookmarkRepository(private val store: FakeStore) : BookmarkRepository {
    var calls = 0
        private set

    override suspend fun setBookmarked(repoId: Long, bookmarked: Boolean) {
        calls++
        store.repos.update { repos ->
            val repo = repos[repoId] ?: return@update repos
            repos + (repoId to repo.copy(isBookmarked = bookmarked))
        }
    }
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
