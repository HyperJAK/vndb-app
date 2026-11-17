package com.zc.vndb.feature.browse.presentation.component.scopeheader.mapper

import com.zc.vndb.common.domain.model.TagCategory
import com.zc.vndb.common.ui.mapper.bbCodeToPlainText
import com.zc.vndb.common.ui.resource.PluralResource
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.ProducerInfo
import com.zc.vndb.feature.browse.domain.model.ProducerType
import com.zc.vndb.feature.browse.domain.model.TagInfo
import com.zc.vndb.feature.browse.presentation.component.scopeheader.model.ScopeHeaderUIModel
import java.util.Locale

fun TagInfo.toHeaderUIModel(): ScopeHeaderUIModel = ScopeHeaderUIModel(
    name = UiText.StringValue(name),
    originalName = null,
    typeLabel = category?.let {
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
    aliasesLabel = aliases.toAliasesLabel(),
    aboutTitle = UiText.StringResource(StringResource.tag_about),
    description = description.toPlainDescription(),
)

fun ProducerInfo.toHeaderUIModel(locale: Locale = Locale.getDefault()): ScopeHeaderUIModel =
    ScopeHeaderUIModel(
        name = UiText.StringValue(name),
        originalName = originalName?.let(UiText::StringValue),
        typeLabel = toTypeLabel(locale),
        isSexualCategory = false,
        countLabel = null,
        aliasesLabel = aliases.toAliasesLabel(),
        aboutTitle = UiText.StringResource(StringResource.producer_about),
        description = description.toPlainDescription(),
    )

/** "Company · Japanese", or whichever of the two VNDB has. */
private fun ProducerInfo.toTypeLabel(locale: Locale): UiText? {
    val typeRes = when (type) {
        ProducerType.Company -> StringResource.producer_type_company
        ProducerType.Individual -> StringResource.producer_type_individual
        ProducerType.AmateurGroup -> StringResource.producer_type_amateur
        null -> null
    }
    val languageName = language
        ?.let { Locale.forLanguageTag(it).getDisplayLanguage(locale) }
        ?.takeIf { it.isNotBlank() }
    return when {
        typeRes != null && languageName != null ->
            UiText.StringResource(
                StringResource.producer_type_with_language,
                listOf(UiText.StringResource(typeRes), languageName),
            )

        typeRes != null -> UiText.StringResource(typeRes)

        languageName != null -> UiText.StringValue(languageName)

        else -> null
    }
}

private fun List<String>.toAliasesLabel(): UiText? = takeIf { it.isNotEmpty() }
    ?.let { UiText.StringResource(StringResource.tag_aliases, listOf(it.joinToString(", "))) }

private fun String?.toPlainDescription(): UiText? = this
    ?.bbCodeToPlainText(keepParagraphs = true, showSpoilers = true)
    ?.takeIf { it.isNotEmpty() }
    ?.let(UiText::StringValue)
