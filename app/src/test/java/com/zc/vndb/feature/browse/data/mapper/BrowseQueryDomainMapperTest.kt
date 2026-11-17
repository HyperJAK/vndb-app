package com.zc.vndb.feature.browse.data.mapper

import com.zc.vndb.common.data.network.appJson
import com.zc.vndb.feature.browse.domain.model.AgeRating
import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowseScope
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import com.zc.vndb.feature.browse.domain.model.Genre
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

class BrowseQueryDomainMapperTest {

    private fun json(text: String) = appJson.parseToJsonElement(text)

    private fun BrowseFilter.body(page: Int = 1, withCount: Boolean = true): JsonObject =
        appJson.encodeToJsonElement(
            toQueryDTO(page = page, pageSize = 20, withCount = withCount),
        ).jsonObject

    @Test
    fun `default filter excludes adult releases, sorted by popularity, with count`() {
        val body = BrowseFilter().body()

        assertEquals(json("""["release","!=",["minage",">=",18]]"""), body["filters"])
        assertEquals(json("\"votecount\""), body["sort"])
        assertEquals(json("true"), body["reverse"])
        assertEquals(json("20"), body["results"])
        assertEquals(json("1"), body["page"])
        assertEquals(json("true"), body["count"])
    }

    @Test
    fun `adult, search and genres are combined with and`() {
        val filter = BrowseFilter(
            query = "steins",
            ageRating = AgeRating.Adult,
            genres = setOf(Genre.Mystery, Genre.Romance),
            sort = BrowseSort.Relevance,
        )

        val body = filter.body()

        assertEquals(
            json(
                """["and",["release","=",["minage",">=",18]],["search","=","steins"],""" +
                    """["tag","=","g96"],["tag","=","g19"]]""",
            ),
            body["filters"],
        )
        assertEquals(json("\"searchrank\""), body["sort"])
        assertEquals(json("false"), body["reverse"])
    }

    @Test
    fun `each sort maps to its api field and direction`() {
        val expected = mapOf(
            BrowseSort.Popularity to ("votecount" to true),
            BrowseSort.Rating to ("rating" to true),
            BrowseSort.Newest to ("released" to true),
            BrowseSort.Title to ("title" to false),
        )
        expected.forEach { (sort, fieldAndReverse) ->
            val body = BrowseFilter(sort = sort).body()
            assertEquals(json("\"${fieldAndReverse.first}\""), body["sort"])
            assertEquals(json("${fieldAndReverse.second}"), body["reverse"])
        }
    }

    @Test
    fun `later pages omit count`() {
        val body = BrowseFilter().body(page = 3, withCount = false)

        assertEquals(json("3"), body["page"])
        assertFalse("count" in body)
    }

    @Test
    fun `newest only includes released titles`() {
        val body = BrowseFilter(sort = BrowseSort.Newest).body()

        assertEquals(
            json("""["and",["release","!=",["minage",">=",18]],["released","<=","today"]]"""),
            body["filters"],
        )
    }

    @Test
    fun `a tag page adds its tag to the filters`() {
        val body = BrowseFilter(scope = BrowseScope.Tag("g249")).body()

        assertEquals(
            json("""["and",["release","!=",["minage",">=",18]],["tag","=","g249"]]"""),
            body["filters"],
        )
    }

    @Test
    fun `a producer page filters to novels the producer developed`() {
        val body = BrowseFilter(scope = BrowseScope.Producer("p30")).body()

        assertEquals(
            json(
                """["and",["release","!=",["minage",">=",18]],["developer","=",["id","=","p30"]]]""",
            ),
            body["filters"],
        )
    }

    @Test
    fun `producer scope combines with search and genres`() {
        val body = BrowseFilter(
            ageRating = AgeRating.Any,
            query = "fate",
            genres = setOf(Genre.Mystery),
            scope = BrowseScope.Producer("p30"),
        ).body()

        assertEquals(
            json(
                """["and",["search","=","fate"],["tag","=","${Genre.Mystery.tagId}"],""" +
                    """["developer","=",["id","=","p30"]]]""",
            ),
            body["filters"],
        )
    }

    @Test
    fun `producer lookup by id`() {
        val body = appJson.encodeToJsonElement(producerQuery("p30")).jsonObject

        assertEquals(json("""["id","=","p30"]"""), body["filters"])
        assertEquals(json("\"name, original, aliases, lang, type, description\""), body["fields"])
    }

    @Test
    fun `tag lookup by id`() {
        val body = appJson.encodeToJsonElement(tagQuery("g249")).jsonObject

        assertEquals(json("""["id","=","g249"]"""), body["filters"])
        assertEquals(json("\"name, aliases, description, category, vn_count\""), body["fields"])
    }

    @Test
    fun `both ages alone is an empty filter list, which VNDB accepts`() {
        assertEquals(json("[]"), BrowseFilter(ageRating = AgeRating.Any).body()["filters"])
        assertEquals(
            json("""["search","=","steins"]"""),
            BrowseFilter(ageRating = AgeRating.Any, query = "steins").body()["filters"],
        )
    }
}
