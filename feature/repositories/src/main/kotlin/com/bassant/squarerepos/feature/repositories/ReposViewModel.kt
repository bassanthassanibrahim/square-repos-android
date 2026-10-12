package com.bassant.squarerepos.feature.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.core.domain.usecase.GetReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
}
