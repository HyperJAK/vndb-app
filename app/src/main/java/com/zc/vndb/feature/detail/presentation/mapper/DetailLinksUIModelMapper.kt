package com.zc.vndb.feature.detail.presentation.mapper

import com.zc.vndb.common.ui.component.linksrow.model.LinkUIModel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/** The VN's page on vndb.org. */
fun vndbUrl(id: String): String = "https://vndb.org/$id"

/** vndb.org first, then the VN's external links. */
fun NovelDetail.toLinks(): ImmutableList<LinkUIModel> = buildList {
    add(
        LinkUIModel(
            UiText.StringResource(StringResource.detail_vndb_link),
            vndbUrl(id),
            isVndb = true,
        ),
    )
    links.forEach { add(LinkUIModel(UiText.StringValue(it.label), it.url, isVndb = false)) }
}.toImmutableList()
