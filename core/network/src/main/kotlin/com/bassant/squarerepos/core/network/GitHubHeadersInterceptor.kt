package com.bassant.squarerepos.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds the headers GitHub recommends to every request, plus the optional auth token
 * (which raises the rate limit from 60 to 5,000 requests/hour).
 */
internal class GitHubHeadersInterceptor(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .apply { if (token.isNotBlank()) header("Authorization", "Bearer $token") }
            .build()
        return chain.proceed(request)
    }
}
