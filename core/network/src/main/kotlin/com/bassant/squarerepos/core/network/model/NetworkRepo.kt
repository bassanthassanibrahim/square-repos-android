package com.bassant.squarerepos.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A repository exactly as returned by `GET /orgs/{org}/repos`.
 * Only the fields the app uses are declared; unknown JSON keys are ignored.
 */
@Serializable
data class NetworkRepo(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String? = null,
    @SerialName("stargazers_count") val stargazersCount: Int = 0,
    @SerialName("forks_count") val forksCount: Int = 0,
    @SerialName("language") val language: String? = null,
    @SerialName("html_url") val htmlUrl: String,
)
