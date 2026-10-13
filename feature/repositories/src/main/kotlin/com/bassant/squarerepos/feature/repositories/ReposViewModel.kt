package com.bassant.squarerepos.feature.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import androidx.paging.cachedIn
import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.core.domain.usecase.GetReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Exposes the paged repository list.
 *
 * `cachedIn(viewModelScope)` keeps the loaded pages across configuration changes,
 * so rotating the device never reloads the list.
 */
@HiltViewModel
class ReposViewModel @Inject constructor(
    getRepos: GetReposUseCase,
) : ViewModel() {

    val repos: Flow<PagingData<Repo>> = getRepos().cachedIn(viewModelScope)

    /**
     * Keeps [repos] loaded while the list is off screen. It draws nothing.
     *
     * Why: toggling a bookmark on the details screen writes to the `bookmarks` table, so Room
     * invalidates the list while the user is away. `cachedIn` loads the new generation only when
     * something collects it, so without this the list would come back empty: a skeleton flash and
     * no card for the shared-element transition to return to.
     */
    private val keepLoaded = object : PagingDataPresenter<Repo>() {
        override suspend fun presentPagingDataEvent(event: PagingDataEvent<Repo>) = Unit
    }

    init {
        viewModelScope.launch {
            repos.collectLatest { pagingData -> keepLoaded.collectFrom(pagingData) }
        }
    }
}
