package com.bassant.squarerepos.core.domain.usecase

import com.bassant.squarerepos.core.domain.repository.BookmarkRepository
import javax.inject.Inject

/**
 * Adds or removes a bookmark.
 *
 * It takes the *target* state instead of "toggle", so a double tap can never
 * leave the bookmark in an unexpected state (the operation is idempotent).
 */
class SetBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(repoId: Long, bookmarked: Boolean) =
        bookmarkRepository.setBookmarked(repoId, bookmarked)
}
