package com.bassant.squarerepos.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// A calm palette inspired by GitHub: soft neutrals, one blue accent and a warm gold
// (tertiary) used only for stars and bookmarks so they stand out.

internal val LightColors = lightColorScheme(
    primary = Color(0xFF3B6FD6),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCE6FB),
    onPrimaryContainer = Color(0xFF0F2A5C),
    secondary = Color(0xFF5B6577),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE4E8EF),
    onSecondaryContainer = Color(0xFF1F2733),
    tertiary = Color(0xFFB7791F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFF1D6),
    onTertiaryContainer = Color(0xFF4A3000),
    background = Color(0xFFF6F8FA),
    onBackground = Color(0xFF1F2328),
    surface = Color(0xFFF6F8FA),
    onSurface = Color(0xFF1F2328),
    surfaceVariant = Color(0xFFEEF1F5),
    onSurfaceVariant = Color(0xFF59636E),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFFFFF),
    surfaceContainer = Color(0xFFF1F4F8),
    surfaceContainerHigh = Color(0xFFEAEEF3),
    surfaceContainerHighest = Color(0xFFE3E8EE),
    outline = Color(0xFFD0D7DE),
    outlineVariant = Color(0xFFE1E6EB),
    error = Color(0xFFCF222E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFE2E0),
    onErrorContainer = Color(0xFF5C0A10),
)

internal val DarkColors = darkColorScheme(
    primary = Color(0xFF8AB0F7),
    onPrimary = Color(0xFF0B1F44),
    primaryContainer = Color(0xFF1E3A74),
    onPrimaryContainer = Color(0xFFDCE6FB),
    secondary = Color(0xFFAEB7C5),
    onSecondary = Color(0xFF1B2230),
    secondaryContainer = Color(0xFF2B3340),
    onSecondaryContainer = Color(0xFFDDE3EC),
    tertiary = Color(0xFFE3B341),
    onTertiary = Color(0xFF3D2A00),
    tertiaryContainer = Color(0xFF4A3608),
    onTertiaryContainer = Color(0xFFFFE6B0),
    background = Color(0xFF0D1117),
    onBackground = Color(0xFFE6EDF3),
    surface = Color(0xFF0D1117),
    onSurface = Color(0xFFE6EDF3),
    surfaceVariant = Color(0xFF21262D),
    onSurfaceVariant = Color(0xFF9198A1),
    surfaceContainerLowest = Color(0xFF090C10),
    surfaceContainerLow = Color(0xFF161B22),
    surfaceContainer = Color(0xFF1A2029),
    surfaceContainerHigh = Color(0xFF21262D),
    surfaceContainerHighest = Color(0xFF2A3038),
    outline = Color(0xFF30363D),
    outlineVariant = Color(0xFF262C34),
    error = Color(0xFFFF7B72),
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF5C1A17),
    onErrorContainer = Color(0xFFFFDAD6),
)

/** GitHub's colors for the most common languages, used for the small dot next to the language name. */
internal val LanguageColors = mapOf(
    "kotlin" to Color(0xFFA97BFF),
    "java" to Color(0xFFB07219),
    "swift" to Color(0xFFF05138),
    "objective-c" to Color(0xFF438EFF),
    "javascript" to Color(0xFFF1E05A),
    "typescript" to Color(0xFF3178C6),
    "go" to Color(0xFF00ADD8),
    "ruby" to Color(0xFF701516),
    "python" to Color(0xFF3572A5),
    "shell" to Color(0xFF89E051),
    "html" to Color(0xFFE34C26),
    "css" to Color(0xFF563D7C),
    "c" to Color(0xFF555555),
    "c++" to Color(0xFFF34B7D),
    "rust" to Color(0xFFDEA584),
    "php" to Color(0xFF4F5D95),
    "starlark" to Color(0xFF76D275),
)
