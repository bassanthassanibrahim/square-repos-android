package com.bassant.squarerepos.core.data

import com.bassant.squarerepos.core.data.mapper.toDataError
import com.bassant.squarerepos.core.domain.error.DataError
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ErrorMapperTest {

    @Test
    fun `IOException becomes NoConnection`() {
        assertTrue(IOException("offline").toDataError() is DataError.NoConnection)
    }

    @Test
    fun `403 with no remaining quota becomes RateLimited with reset time`() {
        val error = httpException(code = 403, "x-ratelimit-remaining" to "0", "x-ratelimit-reset" to "1700000000")
            .toDataError()

        assertTrue(error is DataError.RateLimited)
        assertEquals(1_700_000_000L, (error as DataError.RateLimited).resetAtEpochSeconds)
    }

    @Test
    fun `403 with remaining quota is a server error, not a rate limit`() {
        val error = httpException(code = 403, "x-ratelimit-remaining" to "12").toDataError()

        assertTrue(error is DataError.Server)
        assertEquals(403, (error as DataError.Server).code)
    }

    @Test
    fun `500 becomes Server error`() {
        val error = httpException(code = 500).toDataError()

        assertEquals(500, (error as DataError.Server).code)
    }

    @Test
    fun `unknown exceptions are wrapped`() {
        assertTrue(IllegalStateException().toDataError() is DataError.Unknown)
    }

    private fun httpException(code: Int, vararg headers: Pair<String, String>): HttpException {
        val raw = okhttp3.Response.Builder()
            .code(code)
            .message("HTTP $code")
            .protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url("https://api.github.com/orgs/square/repos").build())
            .apply { headers.forEach { (name, value) -> header(name, value) } }
            .build()
        return HttpException(Response.error<Any>("{}".toResponseBody(), raw))
    }
}
