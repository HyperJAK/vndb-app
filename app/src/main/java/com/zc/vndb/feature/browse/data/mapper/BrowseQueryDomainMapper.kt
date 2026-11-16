package com.zc.vndb.feature.browse.data.mapper

import com.zc.vndb.common.data.dto.QueryDTO
import com.zc.vndb.feature.browse.domain.model.AgeRating
import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

private const val ADULT_MIN_AGE = 18

private const val BROWSE_FIELDS = "title, alttitle, released, rating, length, length_minutes, " +
    "image.url, image.thumbnail, image.sexual, image.violence, developers.name, " +
    "tags.name, tags.rating, tags.spoiler, tags.category"

/** The `POST /vn` body for one page of [BrowseFilter] results. Expects a normalised filter. */
fun BrowseFilter.toQueryDTO(page: Int, pageSize: Int, withCount: Boolean): QueryDTO = QueryDTO(
    filters = toFilters(),
    fields = BROWSE_FIELDS,
    sort = sort.apiField,
    reverse = sort.isDescending,
    results = pageSize,
    page = page,
    count = withCount.takeIf { it },
)

private fun BrowseFilter.toFilters(): JsonElement {
    val adultRelease = predicate("minage", ">=", JsonPrimitive(ADULT_MIN_AGE))
    val predicates = buildList {
        add(
            when (ageRating) {
                AgeRating.AllAges -> predicate("release", "!=", adultRelease)
                AgeRating.Adult -> predicate("release", "=", adultRelease)
            },
        )
        if (query.isNotEmpty()) add(predicate("search", "=", JsonPrimitive(query)))
        // VNDB sorts unknown (TBA) dates last-is-newest; "Newest" should mean already released.
        if (sort == BrowseSort.Newest) add(predicate("released", "<=", JsonPrimitive("today")))
        genres.sortedBy { it.ordinal }.forEach {
            add(
                predicate("tag", "=", JsonPrimitive(it.tagId)),
            )
        }
        tagId?.let { add(predicate("tag", "=", JsonPrimitive(it))) }
    }
    return predicates.singleOrNull() ?: buildJsonArray {
        add("and")
        predicates.forEach { add(it) }
    }
}

private val BrowseSort.apiField: String
    get() = when (this) {
        BrowseSort.Relevance -> "searchrank"
        BrowseSort.Popularity -> "votecount"
        BrowseSort.Rating -> "rating"
        BrowseSort.Newest -> "released"
        BrowseSort.Title -> "title"
    }

private val BrowseSort.isDescending: Boolean
    get() = this == BrowseSort.Popularity || this == BrowseSort.Rating || this == BrowseSort.Newest

private fun predicate(name: String, operator: String, value: JsonElement): JsonArray =
    buildJsonArray {
        add(name)
        add(operator)
        add(value)
    }

/** One tag by id, for the tag page header. */
fun tagQuery(id: String) = QueryDTO(
    filters = predicate("id", "=", JsonPrimitive(id)),
    fields = "name, aliases, description, category, vn_count",
    sort = null,
    reverse = null,
    results = 1,
    page = null,
    count = null,
)
