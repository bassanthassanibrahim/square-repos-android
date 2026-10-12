package com.bassant.squarerepos.core.designsystem.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

/**
 * Shared element transitions between screens of different feature modules.
 *
 * The app's `NavHost` is wrapped in a `SharedTransitionLayout` and every destination provides
 * its `AnimatedVisibilityScope`. Features only call [sharedBoundsIfAvailable], so they stay
 * independent of each other and still work (without animation) in previews and tests.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope?> { null }

/** The `AnimatedVisibilityScope` of the current navigation destination. */
val LocalNavAnimatedVisibilityScope = staticCompositionLocalOf<AnimatedVisibilityScope?> { null }

/** Keys shared between the list and the details screen. */
object SharedElementKeys {
    fun repoCard(repoId: Long) = "repo-card-$repoId"
}

/**
 * Morphs this element into the element with the same [key] on the next screen
 * (a "container transform"). Does nothing when no transition scope is available.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedBoundsIfAvailable(key: Any): Modifier {
    val transitionScope = LocalSharedTransitionScope.current ?: return this
    val visibilityScope = LocalNavAnimatedVisibilityScope.current ?: return this
    return with(transitionScope) {
        this@sharedBoundsIfAvailable.sharedBounds(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = visibilityScope,
        )
    }
}
