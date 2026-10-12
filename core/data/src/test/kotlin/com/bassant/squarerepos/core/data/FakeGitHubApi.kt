package com.bassant.squarerepos.core.data

import com.bassant.squarerepos.core.network.GitHubApi
import com.bassant.squarerepos.core.network.model.NetworkRepo

/** In-memory GitHub API that serves [totalRepos] repositories, page by page. */
class FakeGitHubApi(private val totalRepos: Int) : GitHubApi {

    /** When set, every call throws it (to simulate network failures). */
    var failure: Throwable? = null
    val requestedPages = mutableListOf<Int>()

    override suspend fun getOrgRepos(org: String, page: Int, perPage: Int): List<NetworkRepo> {
        failure?.let { throw it }
        requestedPages += page
        val from = (page - 1) * perPage
        val to = minOf(from + perPage, totalRepos)
        if (from >= to) return emptyList()
        return (from until to).map { index ->
            NetworkRepo(
                id = index.toLong() + 1,
                name = "repo-${index + 1}",
                fullName = "$org/repo-${index + 1}",
                stargazersCount = index * 10,
                htmlUrl = "https://github.com/$org/repo-${index + 1}",
            )
        }
    }
}
