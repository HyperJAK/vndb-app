package com.zc.vndb.feature.browse.presentation.component.tagheader.mapper

import com.zc.vndb.common.domain.model.TagCategory
import com.zc.vndb.common.ui.mapper.bbCodeToPlainText
import com.zc.vndb.common.ui.resource.PluralResource
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.TagInfo
import com.zc.vndb.feature.browse.presentation.component.tagheader.model.TagHeaderUIModel

fun TagInfo.toHeaderUIModel(): TagHeaderUIModel = TagHeaderUIModel(
    name = UiText.StringValue(name),
    categoryLabel = category?.let {
        UiText.StringResource(
            when (it) {
                TagCategory.Content -> StringResource.tag_category_content
                TagCategory.Sexual -> StringResource.tag_category_sexual
                TagCategory.Technical -> StringResource.tag_category_technical
            },
        )
    },
    isSexualCategory = category == TagCategory.Sexual,
    countLabel = UiText.StringPlural(
        PluralResource.tag_vn_count,
        novelCount,
        listOf("%,d".format(novelCount)),
    ),
    aliasesLabel = aliases.takeIf { it.isNotEmpty() }
        ?.let { UiText.StringResource(StringResource.tag_aliases, listOf(it.joinToString(", "))) },
    description = description
        ?.bbCodeToPlainText(keepParagraphs = true, showSpoilers = true)
        ?.takeIf { it.isNotEmpty() }
        ?.let(UiText::StringValue),
)
