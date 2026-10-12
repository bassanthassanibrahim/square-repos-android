package com.bassant.squarerepos.core.domain.usecase

import androidx.paging.PagingData
import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.core.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Returns the paged list of Square's repositories, including each one's bookmark state. */
class GetReposUseCase @Inject constructor(
    private val repoRepository: RepoRepository,
) {
    operator fun invoke(): Flow<PagingData<Repo>> = repoRepository.getRepos()
}
