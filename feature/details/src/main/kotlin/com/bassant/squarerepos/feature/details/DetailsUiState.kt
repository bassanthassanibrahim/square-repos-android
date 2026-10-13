package com.bassant.squarerepos.feature.details

import com.bassant.squarerepos.core.domain.model.Repo

/** Everything the details screen can show. Exactly one state at a time. */
sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data object NotFound : DetailsUiState
    data class Success(val repo: Repo) : DetailsUiState
}
