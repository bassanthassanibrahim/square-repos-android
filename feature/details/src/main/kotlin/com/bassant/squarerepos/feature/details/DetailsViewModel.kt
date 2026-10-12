package com.bassant.squarerepos.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bassant.squarerepos.core.domain.usecase.ObserveRepoUseCase
import com.bassant.squarerepos.feature.details.navigation.DetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Details of one repository.
 *
 * Only the repository **id** travels through navigation (it lives in [SavedStateHandle], so it also
 * survives process death). The repository itself is observed reactively from the database,
 * so the screen always shows the latest saved data.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeRepo: ObserveRepoUseCase,
) : ViewModel() {

    private val repoId: Long = checkNotNull(savedStateHandle[DetailsDestination.ARG_REPO_ID]) {
        "DetailsViewModel requires a repoId navigation argument"
    }

    val uiState: StateFlow<DetailsUiState> = observeRepo(repoId)
        .map { repo -> if (repo == null) DetailsUiState.NotFound else DetailsUiState.Success(repo) }
        .stateIn(
            scope = viewModelScope,
            // Keeps observing for 5s after the UI stops (e.g. rotation) without leaking in the background.
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailsUiState.Loading,
        )
}
