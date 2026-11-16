package com.zc.vndb.feature.release.data.mapper

import com.zc.vndb.common.data.dto.QueryDTO
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

private const val RELEASE_FIELDS =
    "title, alttitle, released, minage, official, patch, freeware, uncensored, has_ero, " +
        "platforms, media.medium, media.qty, resolution, engine, voiced, catalog, gtin, notes, " +
        "languages.lang, languages.title, languages.main, languages.mtl, " +
        "vns.id, vns.title, vns.alttitle, vns.rtype, vns.released, " +
        "vns.image.url, vns.image.thumbnail, vns.image.sexual, " +
        "vns.image.violence, producers.name, producers.developer, producers.publisher, " +
        "images.type, images.url, images.thumbnail, images.sexual, images.violence, extlinks.label, extlinks.url"

fun releaseQuery(id: String) = QueryDTO(
    filters = buildJsonArray {
        add("id")
        add("=")
        add(JsonPrimitive(id))
    },
    fields = RELEASE_FIELDS,
    sort = null,
    reverse = null,
    results = 1,
    page = null,
    count = null,
)
