package com.zc.vndb.common.ui.mapper

private val bothFlags = setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
private val spoilerBlock = Regex("""\[spoiler](.*?)\[/spoiler]""", bothFlags)
private val urlTag = Regex("""\[url=[^\]]*](.*?)\[/url]""", bothFlags)
private val anyTag = Regex("""\[/?[a-z]+(=[^\]]*)?]""", RegexOption.IGNORE_CASE)
private val anyWhitespace = Regex("""\s+""")
private val inlineWhitespace = Regex("""[ \t]+""")
private val paragraphBreak = Regex("""\s*\n\s*\n\s*""")
private val lineBreak = Regex("""\s*\n\s*""")

/**
 * VNDB BBCode → plain text: links keep their text and other tags are dropped.
 * [spoiler] blocks are removed unless [showSpoilers]. With [keepParagraphs] blank-line
 * paragraphs survive (single line breaks become spaces); otherwise it is one line.
 */
fun String.bbCodeToPlainText(
    keepParagraphs: Boolean = false,
    showSpoilers: Boolean = false,
): String {
    val text = this
        .replace(spoilerBlock) { if (showSpoilers) it.groupValues[1] else " " }
        .replace(urlTag, "$1")
        .replace(anyTag, "")
    return if (keepParagraphs) {
        text.split(paragraphBreak)
            .map { it.replace(lineBreak, " ").replace(inlineWhitespace, " ").trim() }
            .filter { it.isNotEmpty() }
            .joinToString("\n\n")
    } else {
        text.replace(anyWhitespace, " ").trim()
    }
}
