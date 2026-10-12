package com.bassant.squarerepos.core.network

import com.bassant.squarerepos.core.network.model.NetworkRepo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** GitHub REST API (v3). Docs: https://docs.github.com/en/rest/repos/repos#list-organization-repositories */
interface GitHubApi {

    /**
     * Lists an organization's public repositories.
     *
     * @param page 1-based page number.
     * @param perPage page size (GitHub allows up to 100).
     */
    @GET("orgs/{org}/repos")
    suspend fun getOrgRepos(
        @Path("org") org: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<NetworkRepo>
}
