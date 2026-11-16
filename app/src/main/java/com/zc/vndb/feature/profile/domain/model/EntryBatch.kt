package com.zc.vndb.feature.profile.domain.model

/** Entries for the list, and where the next ones come from (null at the end). */
data class EntryBatch(val entries: List<ListEntry>, val next: ListCursor?)
