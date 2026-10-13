package com.bassant.squarerepos.feature.repositories.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bassant.squarerepos.core.designsystem.component.ShimmerBlock

/** Skeleton version of [RepoCard], shown while the first page loads. */
@Composable
internal fun RepoCardPlaceholder(modifier: Modifier = Modifier) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row {
                ShimmerBlock(Modifier.width(140.dp).height(18.dp))
                Spacer(Modifier.weight(1f))
                ShimmerBlock(Modifier.width(48.dp).height(18.dp))
            }
            ShimmerBlock(Modifier.fillMaxWidth().height(14.dp))
            ShimmerBlock(Modifier.fillMaxWidth(0.6f).height(14.dp))
            ShimmerBlock(Modifier.width(72.dp).height(12.dp))
        }
    }
}
