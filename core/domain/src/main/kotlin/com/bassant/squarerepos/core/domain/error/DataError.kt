package com.bassant.squarerepos.core.domain.error

/**
 * Errors the data layer can report, independent of Retrofit/OkHttp.
 *
 * The UI maps each case to a user-friendly message, so it never has to know
 * about HTTP status codes or networking libraries.
 */
sealed class DataError(cause: Throwable? = null) : Exception(cause) {

    /** The device is offline or the server could not be reached. */
    class NoConnection(cause: Throwable? = null) : DataError(cause)

    /**
     * GitHub's rate limit (60 requests/hour without a token) was reached.
     * @property resetAtEpochSeconds when the limit resets, if GitHub told us.
     */
    class RateLimited(val resetAtEpochSeconds: Long?) : DataError()

    /** The server answered with an unexpected HTTP status code. */
    class Server(val code: Int) : DataError()

    /** Anything else (e.g. a malformed response). */
    class Unknown(cause: Throwable? = null) : DataError(cause)
}
