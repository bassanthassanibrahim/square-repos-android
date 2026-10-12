package com.bassant.squarerepos.core.data

/** Abstraction over the system clock so time-based logic (cache expiry) is testable. */
fun interface Clock {
    fun nowMillis(): Long
}
