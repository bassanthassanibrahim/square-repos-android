package com.bassant.squarerepos.core.data.mapper

import com.bassant.squarerepos.core.database.model.RepoEntity
import com.bassant.squarerepos.core.database.model.RepoWithBookmark
import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.core.network.model.NetworkRepo

/** Network → database. [position] keeps the API order stable across pages. */
internal fun NetworkRepo.toEntity(position: Int) = RepoEntity(
    id = id,
    name = name,
    fullName = fullName,
    description = description?.takeIf { it.isNotBlank() },
    stars = stargazersCount,
    forks = forksCount,
    language = language,
    htmlUrl = htmlUrl,
    position = position,
)

/** Database → domain. */
internal fun RepoWithBookmark.toDomain() = Repo(
    id = repo.id,
    name = repo.name,
    fullName = repo.fullName,
    description = repo.description,
    stars = repo.stars,
    forks = repo.forks,
    language = repo.language,
    htmlUrl = repo.htmlUrl,
    isBookmarked = isBookmarked,
)
