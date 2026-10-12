package com.bassant.squarerepos.feature.repositories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.bassant.squarerepos.core.designsystem.component.MessageState
import com.bassant.squarerepos.core.domain.model.Repo
import com.bassant.squarerepos.feature.repositories.component.RepoCard
import com.bassant.squarerepos.feature.repositories.component.RepoCardPlaceholder
import kotlinx.coroutines.launch

/** Stateful entry point: connects [ReposViewModel] to the stateless [ReposScreen]. */
@Composable
internal fun ReposRoute(
    onRepoClick: (Long) -> Unit,
    viewModel: ReposViewModel = hiltViewModel(),
) {
    ReposScreen(
        repos = viewModel.repos.collectAsLazyPagingItems(),
        onRepoClick = onRepoClick,
    )
}

/**
 * The repository list. Handles every paging state:
 * - first load → shimmering skeleton cards
 * - first load failed with nothing cached → full-screen error with Retry
 * - refresh failed but cached data exists → list + snackbar with Retry (offline mode)
 * - loading the next page → spinner at the bottom; failure → inline Retry
 * - nothing to show → empty state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReposScreen(
    repos: LazyPagingItems<Repo>,
    onRepoClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val refreshState = repos.loadState.refresh
    val isEmpty = repos.itemCount == 0

    // Offline mode: a refresh failed but we still have saved data → tell the user, offer a retry.
    if (refreshState is LoadState.Error && !isEmpty) {
        val message = stringResource(R.string.repos_showing_saved)
        val retryLabel = stringResource(R.string.repos_retry)
        LaunchedEffect(refreshState) {
            val result = snackbarHostState.showSnackbar(message, retryLabel, duration = SnackbarDuration.Long)
            if (result == SnackbarResult.ActionPerformed) repos.retry()
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.repos_title)) },
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = { ScrollToTopButton(listState) },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = refreshState is LoadState.Loading && !isEmpty,
            onRefresh = repos::refresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            val screenState = when {
                !isEmpty -> ScreenState.Content
                // Wait for Room before deciding the list is empty: offline, the network can fail
                // before the cached rows are read, and the error screen would flash for a frame.
                repos.loadState.source.refresh !is LoadState.NotLoading -> ScreenState.Loading
                refreshState is LoadState.Loading -> ScreenState.Loading
                refreshState is LoadState.Error -> ScreenState.Error
                repos.loadState.append.endOfPaginationReached -> ScreenState.Empty
                else -> ScreenState.Loading
            }
            Crossfade(targetState = screenState, label = "reposScreenState") { state ->
                when (state) {
                    ScreenState.Loading -> SkeletonList()
                    ScreenState.Error -> MessageState(
                        icon = Icons.Rounded.CloudOff,
                        title = stringResource(R.string.repos_error_title),
                        message = (refreshState as? LoadState.Error)?.let { errorMessage(it.error) }.orEmpty(),
                        actionLabel = stringResource(R.string.repos_retry),
                        onAction = repos::retry,
                    )
                    ScreenState.Empty -> MessageState(
                        icon = Icons.Rounded.Inventory2,
                        title = stringResource(R.string.repos_empty_title),
                        message = stringResource(R.string.repos_empty_message),
                    )
                    ScreenState.Content -> RepoList(repos = repos, listState = listState, onRepoClick = onRepoClick)
                }
            }
        }
    }
}

private enum class ScreenState { Loading, Error, Empty, Content }

@Composable
private fun RepoList(
    repos: LazyPagingItems<Repo>,
    listState: LazyListState,
    onRepoClick: (Long) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            count = repos.itemCount,
            key = repos.itemKey { it.id },
            contentType = repos.itemContentType { "repo" },
        ) { index ->
            val repo = repos[index] ?: return@items
            RepoCard(
                repo = repo,
                onClick = { onRepoClick(repo.id) },
                modifier = Modifier.animateItem(),
            )
        }
        item(key = "footer", contentType = "footer") {
            ListFooter(repos)
        }
    }
}

@Composable
private fun ListFooter(repos: LazyPagingItems<Repo>) {
    val append = repos.loadState.append
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            append is LoadState.Loading -> CircularProgressIndicator()
            append is LoadState.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = errorMessage(append.error),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                TextButton(onClick = repos::retry) { Text(stringResource(R.string.repos_retry)) }
            }
            append.endOfPaginationReached && repos.itemCount > 0 -> Text(
                text = pluralStringResource(R.plurals.repos_end_of_list, repos.itemCount, repos.itemCount),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SkeletonList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(6) { RepoCardPlaceholder(Modifier.fillMaxWidth()) }
    }
}

/** Appears once the user has scrolled a bit; smoothly scrolls back to the top. */
@Composable
private fun ScrollToTopButton(listState: LazyListState) {
    val scope = rememberCoroutineScope()
    val showButton by remember { derivedStateOf { listState.firstVisibleItemIndex > 4 } }
    AnimatedVisibility(visible = showButton, enter = scaleIn(), exit = scaleOut()) {
        SmallFloatingActionButton(onClick = { scope.launch { listState.animateScrollToItem(0) } }) {
            Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = stringResource(R.string.repos_scroll_to_top))
        }
    }
}
