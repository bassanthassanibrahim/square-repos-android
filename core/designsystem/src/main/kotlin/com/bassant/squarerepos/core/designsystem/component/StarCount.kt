package com.bassant.squarerepos.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bassant.squarerepos.core.designsystem.util.formatCount

/**
 * A gold star followed by a compact count (e.g. ★ 46.1k).
 * Screen readers hear the exact number ("46123 stars") instead of the abbreviation.
 */
@Composable
fun StarCount(
    stars: Int,
    modifier: Modifier = Modifier,
    iconSize: Dp = 18.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
) {
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = "$stars stars" },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(iconSize),
        )
        Text(text = formatCount(stars), style = textStyle, color = MaterialTheme.colorScheme.onSurface)
    }
}
