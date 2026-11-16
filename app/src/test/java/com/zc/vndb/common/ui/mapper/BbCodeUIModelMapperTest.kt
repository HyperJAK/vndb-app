package com.zc.vndb.common.ui.mapper

import kotlin.test.Test
import kotlin.test.assertEquals

class BbCodeUIModelMapperTest {

    private val raw = "[b]Title[/b]\nLine one\nline two.\n\n\nSee [url=/c1]Okabe[/url]. [spoiler]Twist![/spoiler] End."

    @Test
    fun `one line by default, without spoilers`() {
        assertEquals("Title Line one line two. See Okabe. End.", raw.bbCodeToPlainText())
    }

    @Test
    fun `keeps paragraphs and can show spoilers`() {
        assertEquals(
            "Title Line one line two.\n\nSee Okabe. Twist! End.",
            raw.bbCodeToPlainText(keepParagraphs = true, showSpoilers = true),
        )
    }
}
