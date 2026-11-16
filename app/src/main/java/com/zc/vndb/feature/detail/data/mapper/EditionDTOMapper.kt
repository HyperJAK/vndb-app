package com.zc.vndb.feature.detail.data.mapper

import com.zc.vndb.common.data.mapper.toReleaseDate
import com.zc.vndb.feature.detail.data.dto.EditionDTO
import com.zc.vndb.feature.detail.domain.model.Edition
import com.zc.vndb.feature.detail.domain.model.PlatformFamily

private val pcPlatforms =
    setOf("win", "lin", "mac", "dos", "p88", "p98", "x1s", "x68", "fm7", "fm8", "fmt", "msx", "pce")
private val consolePlatforms = setOf(
    "swi", "ps1", "ps2", "ps3", "ps4", "ps5", "psp", "psv", "xb1", "xb3", "xbo", "xxs",
    "n3d", "nds", "wii", "wiu", "gba", "gbc", "sat", "drc", "sfc", "smd", "scd", "pcf", "vnd",
)
private val mobilePlatforms = setOf("and", "ios", "mob")

/** One edition per platform of the release. */
fun EditionDTO.toDomain(): List<Edition> = platforms.orEmpty().ifEmpty {
    listOf(
        "",
    )
}.map { platform ->
    Edition(
        id = id,
        title = title.orEmpty(),
        releaseDate = released.toReleaseDate(),
        platform = platform,
        family = platform.toPlatformFamily(),
        languageCodes = languages.orEmpty().map { it.lang },
        isOfficial = official ?: false,
        isPatch = patch ?: false,
        minAge = minage,
    )
}

/** Rough family of a VNDB platform code. */
fun String.toPlatformFamily(): PlatformFamily = when (this) {
    in pcPlatforms -> PlatformFamily.Pc
    in consolePlatforms -> PlatformFamily.Console
    in mobilePlatforms -> PlatformFamily.Mobile
    "web" -> PlatformFamily.Web
    else -> PlatformFamily.Other
}
