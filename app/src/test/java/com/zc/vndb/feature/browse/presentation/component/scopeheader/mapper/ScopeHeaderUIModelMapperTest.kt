package com.zc.vndb.feature.browse.presentation.component.scopeheader.mapper

import com.zc.vndb.common.domain.model.TagCategory
import com.zc.vndb.common.ui.resource.PluralResource
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.ProducerInfo
import com.zc.vndb.feature.browse.domain.model.ProducerType
import com.zc.vndb.feature.browse.domain.model.TagInfo
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScopeHeaderUIModelMapperTest {

    private val tag = TagInfo(
        id = "g45",
        name = "Corporations",
        aliases = listOf("Large Companies", "Large Company"),
        description = "A [url=http://en.wikipedia.org/wiki/Corporation]corporation[/url] plays a part.",
        category = TagCategory.Content,
        novelCount = 1136,
    )

    @Test
    fun `maps name, category, count, aliases and plain description`() {
        val header = tag.toHeaderUIModel()

        assertEquals(UiText.StringValue("Corporations"), header.name)
        assertEquals(
            UiText.StringResource(StringResource.tag_category_content),
            header.typeLabel,
        )
        assertEquals(
            UiText.StringPlural(PluralResource.tag_vn_count, 1136, listOf("%,d".format(1136))),
            header.countLabel,
        )
        assertEquals(
            UiText.StringResource(
                StringResource.tag_aliases,
                listOf("Large Companies, Large Company"),
            ),
            header.aliasesLabel,
        )
        assertEquals(UiText.StringValue("A corporation plays a part."), header.description)
    }

    @Test
    fun `sexual tags are flagged and empty parts are null`() {
        val header = tag.copy(
            category = TagCategory.Sexual,
            aliases = emptyList(),
            description = null,
        ).toHeaderUIModel()

        assertTrue(header.isSexualCategory)
        assertNull(header.aliasesLabel)
        assertNull(header.description)
    }

    private val producer = ProducerInfo(
        id = "p30",
        name = "Nitroplus",
        originalName = "ニトロプラス",
        aliases = listOf("Nitro+"),
        type = ProducerType.Company,
        language = "ja",
        description = "Known for [url=/v1]Steins;Gate[/url].",
    )

    @Test
    fun `producer header shows type with language, original name, aliases and plain description`() {
        val header = producer.toHeaderUIModel(Locale.ENGLISH)

        assertEquals(UiText.StringValue("Nitroplus"), header.name)
        assertEquals(UiText.StringValue("ニトロプラス"), header.originalName)
        assertEquals(
            UiText.StringResource(
                StringResource.producer_type_with_language,
                listOf(UiText.StringResource(StringResource.producer_type_company), "Japanese"),
            ),
            header.typeLabel,
        )
        assertNull(header.countLabel)
        assertEquals(
            UiText.StringResource(StringResource.tag_aliases, listOf("Nitro+")),
            header.aliasesLabel,
        )
        assertEquals(UiText.StringResource(StringResource.producer_about), header.aboutTitle)
        assertEquals(UiText.StringValue("Known for Steins;Gate."), header.description)
    }

    @Test
    fun `producer header copes with a missing type, language and extras`() {
        val bare = producer.copy(
            originalName = null,
            aliases = emptyList(),
            type = null,
            language = null,
            description = null,
        ).toHeaderUIModel(Locale.ENGLISH)

        assertNull(bare.typeLabel)
        assertNull(bare.originalName)
        assertNull(bare.aliasesLabel)
        assertNull(bare.description)

        val typeOnly = producer.copy(language = null).toHeaderUIModel(Locale.ENGLISH)
        assertEquals(
            UiText.StringResource(StringResource.producer_type_company),
            typeOnly.typeLabel,
        )
    }
}
