package com.bassant.squarerepos.feature.repositories.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bassant.squarerepos.core.designsystem.component.LocalNavAnimatedVisibilityScope
import com.bassant.squarerepos.feature.repositories.ReposRoute
import kotlinx.serialization.Serializable

/** Type-safe route of the repository list (start destination). */
@Serializable
data object ReposDestination

/** Adds the repository list to the navigation graph. */
fun NavGraphBuilder.reposScreen(onRepoClick: (repoId: Long) -> Unit) {
    composable<ReposDestination> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            ReposRoute(onRepoClick = onRepoClick)
        }
    }
}
