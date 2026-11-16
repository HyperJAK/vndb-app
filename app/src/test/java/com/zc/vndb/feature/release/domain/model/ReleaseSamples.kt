package com.zc.vndb.feature.release.domain.model

import com.zc.vndb.common.domain.model.Cover
import com.zc.vndb.common.domain.model.ExternalLink
import com.zc.vndb.common.domain.model.ReleaseDate
import kotlinx.datetime.LocalDate

object ReleaseSamples {

    fun cover(url: String, sexual: Double = 0.0) = Cover(
        url = url,
        thumbnailUrl = "$url-thumb",
        sexual = sexual,
        violence = 0.0,
    )

    fun novel(id: String, year: Int = 2009, type: ReleaseType = ReleaseType.Complete) =
        ReleaseNovel(
            id = id,
            title = "Novel $id",
            releaseDate = ReleaseDate.Full(LocalDate(year, 1, 1)),
            cover = cover("cover-$id"),
            type = type,
        )

    fun release(
        images: List<ReleaseImage> = emptyList(),
        languages: List<ReleaseLanguage> = listOf(
            ReleaseLanguage("ja", "シュタインズ・ゲート", isMain = true, isMachineTranslated = false),
        ),
        novels: List<ReleaseNovel> = listOf(novel("v2002")),
        links: List<ExternalLink> = emptyList(),
        media: List<ReleaseMedium> = listOf(ReleaseMedium("dvd", 1)),
    ) = ReleaseDetail(
        id = "r7668",
        title = "Steins;Gate",
        altTitle = null,
        releaseDate = ReleaseDate.Full(LocalDate(2010, 8, 26)),
        minAge = 15,
        isOfficial = true,
        isPatch = false,
        isFreeware = false,
        isUncensored = null,
        hasAdultContent = false,
        platforms = listOf("win"),
        media = media,
        resolution = Resolution(1024, 576),
        engine = "N2System",
        voicing = Voicing.Full,
        catalog = "NITRO-102",
        gtin = "4529790134017",
        notes = "Comes with [url=/x]4 trading cards[/url].",
        languages = languages,
        novels = novels,
        producers = listOf(ReleaseProducer("NITRO PLUS", isDeveloper = true, isPublisher = true)),
        images = images,
        links = links,
    )
}
