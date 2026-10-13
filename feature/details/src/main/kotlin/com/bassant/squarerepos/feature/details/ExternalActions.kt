package com.bassant.squarerepos.feature.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

/**
 * Opens [url] in the user's browser.
 * Returns `false` instead of crashing when no installed app can open links, so the screen can say so.
 */
internal fun Context.openInBrowser(url: String): Boolean = try {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    true
} catch (e: ActivityNotFoundException) {
    false
}

/** Shows the system share sheet with the repository name and its GitHub link. */
internal fun Context.shareRepo(name: String, url: String, chooserTitle: String) {
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, name)
        putExtra(Intent.EXTRA_TEXT, "$name\n$url")
    }
    try {
        startActivity(Intent.createChooser(send, chooserTitle))
    } catch (e: ActivityNotFoundException) {
        // The system chooser is always present on real devices; nothing to share with otherwise.
    }
}
