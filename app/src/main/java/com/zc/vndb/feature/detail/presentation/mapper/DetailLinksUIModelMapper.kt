package com.zc.vndb.feature.detail.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.presentation.component.linksrow.model.LinkUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/** The VN's page on vndb.org. */
fun vndbUrl(id: String): String = "https://vndb.org/$id"

/** vndb.org first, then the VN's external links. */
fun NovelDetail.toLinks(): ImmutableList<LinkUIModel> = buildList {
    add(
        LinkUIModel(
            UiText.StringResource(R.string.detail_vndb_link, listOf(id)),
            vndbUrl(id),
            isVndb = true,
        ),
    )
    links.forEach { add(LinkUIModel(UiText.StringValue(it.label), it.url, isVndb = false)) }
}.toImmutableList()
