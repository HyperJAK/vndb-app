package com.zc.vndb.feature.detail.presentation.mapper

import com.zc.vndb.common.ui.mapper.bbCodeToPlainText
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.presentation.model.SpoilerLevel

/** The description as paragraphs; `[spoiler]` passages only from [SpoilerLevel.Minor] up. */
fun NovelDetail.toSynopsis(level: SpoilerLevel): UiText? = description
    ?.bbCodeToPlainText(keepParagraphs = true, showSpoilers = level != SpoilerLevel.None)
    ?.takeIf { it.isNotEmpty() }
    ?.let(UiText::StringValue)
