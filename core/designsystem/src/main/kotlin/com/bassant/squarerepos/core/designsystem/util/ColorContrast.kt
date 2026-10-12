package com.bassant.squarerepos.core.designsystem.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance

/** WCAG contrast ratio between two colors, from 1 (identical) to 21 (black on white). */
fun contrastRatio(a: Color, b: Color): Float {
    val lighter = maxOf(a.luminance(), b.luminance())
    val darker = minOf(a.luminance(), b.luminance())
    return (lighter + 0.05f) / (darker + 0.05f)
}

/**
 * Returns this color, or the closest version of it that reaches [minRatio] against [background]:
 * dark colors are lightened on dark backgrounds, light colors are darkened on light ones.
 * The default 3:1 is the WCAG minimum for meaningful graphics such as icons and dots.
 */
fun Color.withMinContrast(background: Color, minRatio: Float = 3f): Color {
    if (contrastRatio(this, background) >= minRatio) return this
    val towards = if (background.luminance() < 0.5f) Color.White else Color.Black
    var fraction = 0f
    var adjusted = this
    while (contrastRatio(adjusted, background) < minRatio && fraction < 1f) {
        fraction += 0.05f
        adjusted = lerp(this, towards, fraction.coerceAtMost(1f))
    }
    return adjusted
}
