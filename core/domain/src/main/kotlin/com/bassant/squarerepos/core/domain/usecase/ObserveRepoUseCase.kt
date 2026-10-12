package com.bassant.squarerepos.core.domain.usecase

import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.core.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Observes one repository reactively: any change in the database (for example a bookmark
 * toggled anywhere in the app) is emitted again, so screens never show stale data.
 */
class ObserveRepoUseCase @Inject constructor(
    private val repoRepository: RepoRepository,
) {
    operator fun invoke(repoId: Long): Flow<Repo?> = repoRepository.observeRepo(repoId)
}
