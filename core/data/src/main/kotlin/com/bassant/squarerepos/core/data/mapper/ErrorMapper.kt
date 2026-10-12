package com.bassant.squarerepos.core.data.mapper

import com.bassant.squarerepos.core.domain.error.DataError
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/**
 * Translates low-level exceptions (OkHttp, Retrofit, serialization) into [DataError],
 * so nothing above the data layer depends on networking libraries.
 */
internal fun Throwable.toDataError(): DataError = when (this) {
    is DataError -> this
    is HttpException -> toDataError()
    is IOException -> DataError.NoConnection(this)
    is SerializationException -> DataError.Unknown(this)
    else -> DataError.Unknown(this)
}

private fun HttpException.toDataError(): DataError {
    val headers = response()?.headers()
    // GitHub signals a rate limit with 403 or 429 and "x-ratelimit-remaining: 0".
    val isRateLimited = (code() == 403 || code() == 429) &&
        (headers?.get("x-ratelimit-remaining") == "0" || code() == 429)
    return if (isRateLimited) {
        DataError.RateLimited(resetAtEpochSeconds = headers?.get("x-ratelimit-reset")?.toLongOrNull())
    } else {
        DataError.Server(code())
    }
}
