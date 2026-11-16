package com.zc.vndb.feature.detail.data.mapper

import com.zc.vndb.common.data.dto.QueryDTO
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

/* The three queries behind the detail page. */

private const val MAX_RESULTS = 100

private const val NOVEL_FIELDS =
    "title, alttitle, released, rating, average, votecount, length, length_minutes, " +
        "image.url, image.thumbnail, image.sexual, image.violence, developers.name, description, " +
        "tags.name, tags.rating, tags.spoiler, tags.category, extlinks.label, extlinks.url, " +
        "relations.id, relations.title, relations.relation, relations.relation_official, relations.released, " +
        "relations.rating, va.character.id, va.staff.name"

private const val CHARACTER_FIELDS = "name, original, image.url, image.sexual, image.violence, " +
    "vns.id, vns.role, vns.spoiler"

private const val EDITION_FIELDS = "title, released, platforms, languages.lang, official, patch, minage"

fun novelQuery(id: String) = QueryDTO(
    filters = predicate("id", "=", JsonPrimitive(id)),
    fields = NOVEL_FIELDS,
    sort = null,
    reverse = null,
    results = 1,
    page = null,
    count = null,
)

fun charactersQuery(novelId: String) = QueryDTO(
    filters = predicate("vn", "=", predicate("id", "=", JsonPrimitive(novelId))),
    fields = CHARACTER_FIELDS,
    sort = null,
    reverse = null,
    results = MAX_RESULTS,
    page = null,
    count = null,
)

fun editionsQuery(novelId: String) = QueryDTO(
    filters = predicate("vn", "=", predicate("id", "=", JsonPrimitive(novelId))),
    fields = EDITION_FIELDS,
    sort = "released",
    reverse = false,
    results = MAX_RESULTS,
    page = null,
    count = null,
)

private fun predicate(name: String, operator: String, value: JsonElement): JsonArray =
    buildJsonArray {
        add(name)
        add(operator)
        add(value)
    }
