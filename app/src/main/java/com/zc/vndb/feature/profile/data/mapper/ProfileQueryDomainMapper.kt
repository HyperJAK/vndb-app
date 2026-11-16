package com.zc.vndb.feature.profile.data.mapper

import com.zc.vndb.feature.profile.data.dto.ListQueryDTO
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

private const val ENTRY_FIELDS =
    "id, vote, lastmod, started, finished, notes, labels.id, labels.label, " +
        "vn.title, vn.alttitle, vn.released, vn.image.url, vn.image.thumbnail, vn.image.sexual, vn.image.violence"

/** A page of [userId]'s entries with [labelId], most recently changed first. */
fun entriesQuery(userId: String, labelId: Int, page: Int, pageSize: Int) = ListQueryDTO(
    user = userId,
    filters = buildJsonArray {
        add("label")
        add("=")
        add(labelId)
    },
    fields = ENTRY_FIELDS,
    sort = "lastmod",
    reverse = true,
    results = pageSize,
    page = page,
)
