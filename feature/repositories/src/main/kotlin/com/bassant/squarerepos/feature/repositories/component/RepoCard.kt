package com.bassant.squarerepos.feature.repositories.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bassant.squarerepos.core.designsystem.component.LanguageLabel
import com.bassant.squarerepos.core.designsystem.component.SharedElementKeys
import com.bassant.squarerepos.core.designsystem.component.StarCount
import com.bassant.squarerepos.core.designsystem.component.sharedBoundsIfAvailable
import com.bassant.squarerepos.core.designsystem.theme.SquareReposTheme
import com.bassant.squarerepos.core.domain.model.Repo

/**
 * One repository in the list: name, stars, description and language.
 * Micro-interaction: the card gently shrinks while pressed.
 */
@Composable
internal fun RepoCard(
    repo: Repo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "cardPressScale",
    )

    Card(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .sharedBoundsIfAvailable(SharedElementKeys.repoCard(repo.id)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                StarCount(stars = repo.stars, modifier = Modifier.padding(start = 12.dp))
            }

            repo.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            repo.language?.let { LanguageLabel(language = it) }
        }
    }
}

@Preview
@Composable
private fun RepoCardPreview() {
    SquareReposTheme {
        RepoCard(
            repo = Repo(
                id = 1,
                name = "okhttp",
                fullName = "square/okhttp",
                description = "Square’s meticulous HTTP client for the JVM, Android, and GraalVM.",
                stars = 46_123,
                forks = 9_200,
                language = "Kotlin",
                htmlUrl = "https://github.com/square/okhttp",
                isBookmarked = false,
            ),
            onClick = {},
        )
    }
}
