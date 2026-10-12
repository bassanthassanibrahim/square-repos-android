package com.bassant.squarerepos.core.designsystem.util

import kotlin.math.abs

/**
 * Formats large counts the way GitHub does: `999`, `1.2k`, `12.3k`, `1.5M`.
 * Values are rounded **down** (so `999_999` is `999.9k`, never `1000k`) and a trailing `.0` is dropped.
 */
fun formatCount(count: Int): String {
    val value = abs(count.toLong())
    val sign = if (count < 0) "-" else ""
    return when {
        value < 1_000 -> "$sign$value"
        value < 1_000_000 -> sign + tenths(value / 100) + "k"
        else -> sign + tenths(value / 100_000) + "M"
    }
}

/** Integer math only, to avoid floating-point rounding surprises. */
private fun tenths(tenths: Long): String {
    val whole = tenths / 10
    val fraction = tenths % 10
    return if (fraction == 0L) "$whole" else "$whole.$fraction"
}
