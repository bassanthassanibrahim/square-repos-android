package com.bassant.squarerepos.core.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Bookmark icon with a micro-interaction: when it becomes bookmarked it "pops"
 * (a quick bouncy scale) while the outline morphs into the filled gold icon.
 * The pop only plays on user changes, not when the screen first appears.
 */
@Composable
fun AnimatedBookmarkIcon(
    isBookmarked: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val scale = remember { Animatable(1f) }
    var previous by remember { mutableStateOf(isBookmarked) }

    LaunchedEffect(isBookmarked) {
        if (isBookmarked && !previous) {
            scale.snapTo(0.6f)
            scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium))
        }
        previous = isBookmarked
    }

    AnimatedContent(
        targetState = isBookmarked,
        transitionSpec = {
            (fadeIn(tween(150)) + scaleIn(initialScale = 0.8f)) togetherWith (fadeOut(tween(100)) + scaleOut(targetScale = 0.8f))
        },
        modifier = modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        },
        label = "bookmarkIcon",
    ) { bookmarked ->
        Icon(
            imageVector = if (bookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
            contentDescription = contentDescription,
            tint = if (bookmarked) MaterialTheme.colorScheme.tertiary else LocalContentColor.current,
        )
    }
}
