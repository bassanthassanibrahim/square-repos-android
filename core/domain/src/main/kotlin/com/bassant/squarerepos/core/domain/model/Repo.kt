package com.bassant.squarerepos.core.domain.model

/**
 * A public GitHub repository, as the app understands it.
 *
 * This is the only repository model the UI layer sees: network DTOs and database entities
 * are mapped to it at the data-layer boundary.
 *
 * @property id GitHub's unique repository id.
 * @property stars Number of stargazers (`stargazers_count` in the API).
 * @property isBookmarked Whether the user saved this repository locally.
 */
data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val htmlUrl: String,
    val isBookmarked: Boolean,
)
