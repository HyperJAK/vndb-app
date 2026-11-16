package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.ui.text.UiText

private val platformNames = mapOf(
    "win" to "Windows",
    "lin" to "Linux",
    "mac" to "macOS",
    "web" to "Web",
    "and" to "Android",
    "ios" to "iOS",
    "swi" to "Switch",
    "ps1" to "PS1",
    "ps2" to "PS2",
    "ps3" to "PS3",
    "ps4" to "PS4",
    "ps5" to "PS5",
    "psv" to "PS Vita",
    "psp" to "PSP",
    "n3d" to "3DS",
    "nds" to "DS",
    "xb1" to "Xbox",
    "xb3" to "Xbox 360",
    "xbo" to "Xbox One",
    "xxs" to "Xbox Series",
    "dvd" to "DVD",
)

/** Display name of a VNDB platform code ("win" → Windows); unknown codes are upper-cased. */
fun platformName(code: String): String = platformNames[code] ?: code.uppercase()

fun platformLabel(code: String): UiText = UiText.StringValue(platformName(code))
