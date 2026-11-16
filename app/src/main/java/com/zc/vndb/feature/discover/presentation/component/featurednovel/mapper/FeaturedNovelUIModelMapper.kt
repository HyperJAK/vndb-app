package com.zc.vndb.feature.discover.presentation.component.featurednovel.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.discover.domain.model.FeaturedNovel
import com.zc.vndb.feature.discover.presentation.component.featurednovel.model.FeaturedNovelUIModel
import com.zc.vndb.feature.discover.presentation.mapper.toRatingOutOfTenLabel
import kotlinx.collections.immutable.toImmutableList

fun FeaturedNovel.toUIModel(): FeaturedNovelUIModel = FeaturedNovelUIModel(
    id = id,
    title = UiText.StringValue(title),
    ratingLabel = rating?.toRatingOutOfTenLabel(),
    developerLabel = developerName?.let(UiText::StringValue),
    writerLabel = scenarioWriter?.let(UiText::StringValue),
    description = description?.toPlainText()?.takeIf { it.isNotEmpty() }?.let(UiText::StringValue),
    coverUrl = cover?.url,
    isCoverSensitive = cover?.isSensitive ?: false,
    tags = tags.map(UiText::StringValue).toImmutableList(),
    ageLabel = if (isAllAges) UiText.StringResource(R.string.discover_all_ages) else null,
)

private val spoilerBlock = Regex(
    """\[spoiler].*?\[/spoiler]""",
    setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE),
)
private val urlTag = Regex(
    """\[url=[^\]]*](.*?)\[/url]""",
    setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE),
)
private val anyTag = Regex("""\[/?[a-z]+(=[^\]]*)?]""", RegexOption.IGNORE_CASE)
private val whitespace = Regex("""\s+""")

/** VNDB BBCode → one line of plain text: spoilers removed, links reduced to their text, tags dropped. */
internal fun String.toPlainText(): String = this
    .replace(spoilerBlock, " ")
    .replace(urlTag, "$1")
    .replace(anyTag, "")
    .replace(whitespace, " ")
    .trim()
