package com.zc.vndb.feature.discover.data.remote

import com.zc.vndb.common.data.network.appJson
import com.zc.vndb.feature.discover.data.mapper.firstNovelFromQuery
import com.zc.vndb.feature.discover.data.mapper.latestNovelQuery
import com.zc.vndb.feature.discover.data.mapper.novelDetailsQuery
import com.zc.vndb.feature.discover.data.mapper.recentReleasesQuery
import com.zc.vndb.feature.discover.data.mapper.topRatedNovelsQuery
import com.zc.vndb.feature.discover.data.remote.DiscoverApiStub.Companion.fixture
import io.ktor.http.HttpMethod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.jsonObject

class DiscoverServiceTest {

    private val api = DiscoverApiStub()

    private fun json(text: String) = appJson.parseToJsonElement(text)

    @Test
    fun `top rated posts a rating-sorted vote-filtered query to vn`() = runTest {
        api.body = fixture("top_rated.json")

        val page = api.service.queryNovels(topRatedNovelsQuery(limit = 50))

        val request = api.requests.single()
        assertEquals(HttpMethod.Post, request.method)
        assertEquals("/kana/vn", request.path)
        assertEquals(
            json(
                """
                {"filters":["votecount",">=",500],
                 "fields":"title, alttitle, rating, image.url, image.thumbnail, image.sexual, image.violence, developers.name",
                 "sort":"rating","reverse":true,"results":50}
                """,
            ),
            request.body,
        )
        assertEquals(listOf("v20802", "v2016"), page.results.take(2).map { it.id })
        assertEquals("Alice Soft", page.results.first().developers?.single()?.name)
    }

    @Test
    fun `novel details asks for one id with staff and tags`() = runTest {
        api.body = fixture("details.json")

        val novel = api.service.queryNovels(novelDetailsQuery("v2002")).results.single()

        val body = api.requests.single().body
        assertEquals(
            json(
                """
                {"filters":["id","=","v2002"],
                 "fields":"title, alttitle, rating, image.url, image.thumbnail, image.sexual, image.violence, developers.name, description, staff.name, staff.role, tags.name, tags.rating, tags.spoiler, tags.category",
                 "results":1}
                """,
            ),
            body,
        )
        assertEquals("STEINS;GATE", novel.title)
        assertEquals("scenario", novel.staff?.get(1)?.role)
        assertEquals("tech", novel.tags?.first()?.category)
    }

    @Test
    fun `recent releases posts a released and language filter to release`() = runTest {
        api.body = fixture("releases.json")

        val page = api.service.queryReleases(recentReleasesQuery(listOf("en", "ja"), limit = 30))

        val request = api.requests.single()
        assertEquals("/kana/release", request.path)
        assertEquals(
            json(
                """
                ["and",["released","<=","today"],["or",["lang","=","en"],["lang","=","ja"]]]
                """,
            ),
            request.body?.jsonObject?.get("filters"),
        )
        val first = page.results.first()
        assertEquals("2026-11-16", first.released)
        assertEquals("v63472", first.vns?.single()?.id)
        assertEquals(false, first.patch)
    }

    @Test
    fun `latest and first-from queries sort by id`() = runTest {
        api.body = """{"more":true,"results":[{"id":"v70743"}]}"""

        val latest = api.service.queryNovels(latestNovelQuery()).results.single()
        api.service.queryNovels(firstNovelFromQuery(30000))

        assertEquals("v70743", latest.id)
        assertNull(latest.title)
        assertEquals(
            json("""{"filters":[],"fields":"id","sort":"id","reverse":true,"results":1}"""),
            api.requests[0].body,
        )
        assertEquals(
            json(
                """{"filters":["id",">=","v30000"],"fields":"id","sort":"id","reverse":false,"results":1}""",
            ),
            api.requests[1].body,
        )
    }

    @Test
    fun `stats is a GET without a body`() = runTest {
        api.body = fixture("stats.json")

        val stats = api.service.getStats()

        assertEquals(HttpMethod.Get, api.requests.single().method)
        assertEquals("/kana/stats", api.requests.single().path)
        assertEquals(68053, stats.vn)
    }
}
