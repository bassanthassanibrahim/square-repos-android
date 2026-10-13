package com.bassant.squarerepos.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bassant.squarerepos.core.domain.usecase.ObserveRepoUseCase
import com.bassant.squarerepos.core.domain.usecase.SetBookmarkUseCase
import com.bassant.squarerepos.feature.details.navigation.DetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Details of one repository.
 *
 * Only the repository **id** travels through navigation (it lives in [SavedStateHandle], so it also
 * survives process death). The repository itself is observed reactively from the database:
 * when its bookmark changes, the screen updates by itself.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeRepo: ObserveRepoUseCase,
    private val setBookmark: SetBookmarkUseCase,
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

    /** Flips the bookmark. Sends the *target* state, so fast double taps stay consistent. */
    fun onBookmarkClick() {
        val repo = (uiState.value as? DetailsUiState.Success)?.repo ?: return
        viewModelScope.launch {
            setBookmark(repoId = repo.id, bookmarked = !repo.isBookmarked)
        }
    }
}
