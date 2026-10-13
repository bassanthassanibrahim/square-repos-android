package com.bassant.squarerepos.feature.details.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bassant.squarerepos.core.designsystem.component.LocalNavAnimatedVisibilityScope
import com.bassant.squarerepos.feature.details.DetailsRoute
import kotlinx.serialization.Serializable

/** Type-safe route of the details screen. Only the id is passed, never the whole object. */
@Serializable
data class DetailsDestination(val repoId: Long) {
    companion object {
        /** Name of the argument in `SavedStateHandle` (matches the property name above). */
        const val ARG_REPO_ID = "repoId"
    }
}

/** Opens the details of [repoId]. `launchSingleTop` prevents duplicate screens on fast double taps. */
fun NavController.navigateToDetails(repoId: Long) {
    navigate(DetailsDestination(repoId)) { launchSingleTop = true }
}

/** Adds the details screen to the navigation graph. */
fun NavGraphBuilder.detailsScreen(onBack: () -> Unit) {
    composable<DetailsDestination> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            DetailsRoute(onBack = onBack)
        }
    }
}
