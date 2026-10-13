package com.bassant.squarerepos.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bassant.squarerepos.core.designsystem.component.LocalSharedTransitionScope
import com.bassant.squarerepos.feature.details.navigation.detailsScreen
import com.bassant.squarerepos.feature.details.navigation.navigateToDetails
import com.bassant.squarerepos.feature.repositories.navigation.ReposDestination
import com.bassant.squarerepos.feature.repositories.navigation.reposScreen

/**
 * App navigation graph. The app module is the only place that knows about all features:
 * features expose their routes and graph builders, and are wired together here.
 *
 * `SharedTransitionLayout` enables the card → details container transform.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SquareReposNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {
        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                NavHost(navController = navController, startDestination = ReposDestination) {
                    reposScreen(onRepoClick = navController::navigateToDetails)
                    detailsScreen(onBack = { navController.navigateUp() })
                }
            }
        }
    }
}
