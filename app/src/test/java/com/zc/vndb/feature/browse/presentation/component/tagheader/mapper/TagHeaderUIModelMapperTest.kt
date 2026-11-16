package com.zc.vndb.feature.browse.presentation.component.tagheader.mapper

import com.zc.vndb.common.domain.model.TagCategory
import com.zc.vndb.common.ui.resource.PluralResource
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.TagInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TagHeaderUIModelMapperTest {

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
            header.categoryLabel,
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
}
