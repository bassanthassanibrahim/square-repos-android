package com.bassant.squarerepos.core.network

import com.bassant.squarerepos.core.network.model.NetworkRepo
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NetworkRepoParsingTest {

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `parses the fields the app needs and ignores the rest`() {
        val body = """
            [
              {
                "id": 1059,
                "node_id": "MDEwOlJlcG9zaXRvcnkxMDU5",
                "name": "okhttp",
                "full_name": "square/okhttp",
                "description": "Square’s meticulous HTTP client.",
                "stargazers_count": 46123,
                "forks_count": 9200,
                "language": "Kotlin",
                "html_url": "https://github.com/square/okhttp",
                "owner": { "login": "square" }
              }
            ]
        """.trimIndent()

        val repos = json.decodeFromString<List<NetworkRepo>>(body)

        assertEquals(1, repos.size)
        with(repos.first()) {
            assertEquals(1059L, id)
            assertEquals("okhttp", name)
            assertEquals("square/okhttp", fullName)
            assertEquals(46123, stargazersCount)
            assertEquals(9200, forksCount)
            assertEquals("Kotlin", language)
        }
    }

    @Test
    fun `missing optional fields fall back to defaults`() {
        val body = """[{ "id": 7, "name": "x", "full_name": "square/x", "html_url": "u", "description": null }]"""

        val repo = json.decodeFromString<List<NetworkRepo>>(body).first()

        assertNull(repo.description)
        assertNull(repo.language)
        assertEquals(0, repo.stargazersCount)
    }
}
