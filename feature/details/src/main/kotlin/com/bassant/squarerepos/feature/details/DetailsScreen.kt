package com.bassant.squarerepos.feature.details

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bassant.squarerepos.core.designsystem.component.LanguageLabel
import com.bassant.squarerepos.core.designsystem.component.MessageState
import com.bassant.squarerepos.core.designsystem.component.SharedElementKeys
import com.bassant.squarerepos.core.designsystem.component.sharedBoundsIfAvailable
import com.bassant.squarerepos.core.designsystem.theme.SquareReposTheme
import com.bassant.squarerepos.core.designsystem.util.formatCount
import com.bassant.squarerepos.core.domain.model.Repo
import kotlinx.coroutines.launch

/** Stateful entry point: connects [DetailsViewModel] to the stateless [DetailsScreen]. */
@Composable
internal fun DetailsRoute(
    onBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailsScreen(
        uiState = uiState,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsScreen(
    uiState: DetailsUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val noBrowserMessage = stringResource(R.string.details_no_browser)
    val shareTitle = stringResource(R.string.details_share_chooser)
    val openInBrowser: (String) -> Unit = { url ->
        if (!context.openInBrowser(url)) scope.launch { snackbarHostState.showSnackbar(noBrowserMessage) }
    }
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is DetailsUiState.Success) {
                        Text(uiState.repo.fullName, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.details_back))
                    }
                },
                actions = {
                    if (uiState is DetailsUiState.Success) {
                        IconButton(
                            onClick = { context.shareRepo(uiState.repo.fullName, uiState.repo.htmlUrl, shareTitle) },
                        ) {
                            Icon(Icons.Rounded.Share, contentDescription = stringResource(R.string.details_share))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (uiState) {
                DetailsUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                DetailsUiState.NotFound -> MessageState(
                    icon = Icons.Rounded.SearchOff,
                    title = stringResource(R.string.details_not_found_title),
                    message = stringResource(R.string.details_not_found_message),
                )
                is DetailsUiState.Success -> DetailsContent(
                    repo = uiState.repo,
                    onOpenInBrowser = { openInBrowser(uiState.repo.htmlUrl) },
                )
            }
        }
    }
}

@Composable
private fun DetailsContent(
    repo: Repo,
    onOpenInBrowser: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HeaderCard(repo)
        StatsRow(repo, Modifier.enterAfterSharedCard(delayMillis = STATS_ENTER_DELAY_MS))
        OpenInBrowserButton(
            onClick = onOpenInBrowser,
            modifier = Modifier.enterAfterSharedCard(delayMillis = ACTIONS_ENTER_DELAY_MS),
        )
    }
}

@Composable
private fun OpenInBrowserButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Icon(Icons.Rounded.Public, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.details_open_in_browser))
    }
}

// The header card morphs from the list (shared bounds) as soon as the repository is loaded.
// Everything below it waits for that movement, then fades and slides up in order:
// stats first, then the actions.
private const val STATS_ENTER_DELAY_MS = 200
private const val ACTIONS_ENTER_DELAY_MS = 280

/**
 * Fades and slides this element in [delayMillis] after it first appears, so it never draws over
 * the moving header card. Plays once per screen (not again after rotation).
 */
@Composable
private fun Modifier.enterAfterSharedCard(delayMillis: Int): Modifier {
    var played by rememberSaveable { mutableStateOf(false) }
    val progress = remember { Animatable(if (played) 1f else 0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(durationMillis = 300, delayMillis = delayMillis, easing = FastOutSlowInEasing))
        played = true
    }
    return graphicsLayer {
        alpha = progress.value
        translationY = (1f - progress.value) * 24.dp.toPx()
    }
}

/** Same container as the list card, so the shared transition morphs one into the other. */
@Composable
private fun HeaderCard(repo: Repo) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .sharedBoundsIfAvailable(SharedElementKeys.repoCard(repo.id)),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = repo.fullName.substringBefore('/') + " /",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = repo.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = repo.description ?: stringResource(R.string.details_no_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StatsRow(repo: Repo, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier.fillMaxWidth()) {
        StatTile(
            icon = Icons.Rounded.Star,
            label = stringResource(R.string.details_stars),
            modifier = Modifier.weight(1f),
            highlight = true,
        ) { AnimatedCount(repo.stars) }
        StatTile(
            icon = Icons.Rounded.Code,
            label = stringResource(R.string.details_forks),
            modifier = Modifier.weight(1f),
        ) { AnimatedCount(repo.forks) }
        repo.language?.let { language ->
            StatTile(
                icon = null,
                label = stringResource(R.string.details_language),
                modifier = Modifier.weight(1f),
            ) { LanguageLabel(language) }
        }
    }
}

@Composable
private fun StatTile(
    icon: ImageVector?,
    label: String,
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    value: @Composable () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (highlight) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
        ),
        modifier = modifier,
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (highlight) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                }
                Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            value()
        }
    }
}

/** Counts up from 0 to [target] when the screen opens – a small "alive" detail. */
@Composable
private fun AnimatedCount(target: Int) {
    val animated = remember { Animatable(0f) }
    LaunchedEffect(target) {
        animated.animateTo(target.toFloat(), tween(durationMillis = 900, easing = FastOutSlowInEasing))
    }
    Text(
        text = formatCount(animated.value.toInt()),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.semantics { stateDescription = target.toString() },
    )
}

@Preview
@Composable
private fun DetailsScreenPreview() {
    SquareReposTheme {
        DetailsScreen(
            uiState = DetailsUiState.Success(
                Repo(
                    id = 1,
                    name = "retrofit",
                    fullName = "square/retrofit",
                    description = "A type-safe HTTP client for Android and the JVM",
                    stars = 43_100,
                    forks = 7_300,
                    language = "Kotlin",
                    htmlUrl = "https://github.com/square/retrofit",
                    isBookmarked = false,
                ),
            ),
            onBack = {},
        )
    }
}
