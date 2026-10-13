package com.bassant.squarerepos.feature.repositories

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bassant.squarerepos.core.domain.error.DataError
import java.text.DateFormat
import java.util.Date

/** Turns any loading error into a short, friendly sentence for the user. */
@Composable
internal fun errorMessage(error: Throwable): String = when (error) {
    is DataError.NoConnection -> stringResource(R.string.repos_error_no_connection)
    is DataError.RateLimited -> error.resetAtEpochSeconds
        ?.let { seconds ->
            val time = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(seconds * 1_000))
            stringResource(R.string.repos_error_rate_limited_until, time)
        }
        ?: stringResource(R.string.repos_error_rate_limited)
    is DataError.Server -> stringResource(R.string.repos_error_server, error.code)
    else -> stringResource(R.string.repos_error_unknown)
}

/** Snackbar text when a refresh failed but saved repositories are still on screen. */
@StringRes
internal fun savedDataMessage(error: Throwable): Int = when (error) {
    is DataError.NoConnection -> R.string.repos_showing_saved_offline
    is DataError.RateLimited -> R.string.repos_showing_saved_rate_limited
    else -> R.string.repos_showing_saved
}
