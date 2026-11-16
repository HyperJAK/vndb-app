package com.zc.vndb.feature.discover.domain.model

import com.zc.vndb.common.domain.model.Cover
import com.zc.vndb.common.domain.model.NovelTag
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.domain.model.TagCategory
import kotlinx.datetime.LocalDate

/** Builders for Discover domain models with sensible defaults, for tests. */
object DiscoverSamples {

    val safeCover = Cover(
        url = "https://t.vndb.org/cv/1.jpg",
        thumbnailUrl = null,
        sexual = 0.0,
        violence = 0.0,
    )

    fun novel(id: String, rating: Double? = 85.0) = DiscoverNovel(
        id = id,
        title = "Novel $id",
        rating = rating,
        cover = safeCover,
        developerName = "Studio",
    )

    fun details(
        id: String = "v1",
        cover: Cover? = safeCover,
        staff: List<StaffCredit> = emptyList(),
        tags: List<NovelTag> = emptyList(),
        developers: List<String> = listOf("TYPE-MOON"),
    ) = NovelDetails(
        id = id,
        title = "Novel $id",
        rating = 89.2,
        cover = cover,
        description = "Description",
        developerNames = developers,
        staff = staff,
        tags = tags,
    )

    fun tag(
        name: String,
        rating: Double = 2.0,
        spoiler: Int = 0,
        category: TagCategory = TagCategory.Content,
    ) = NovelTag(name = name, rating = rating, spoilerLevel = spoiler, category = category)

    fun date(year: Int, month: Int, day: Int): ReleaseDate = ReleaseDate.Full(
        LocalDate(year, month, day),
    )

    fun release(
        id: String,
        novelId: String = "v1",
        released: ReleaseDate = date(2024, 10, 1),
        novelReleased: ReleaseDate = released,
        originalLanguage: String = "ja",
        languages: List<ReleaseLanguage> = listOf(
            ReleaseLanguage("ja", isMachineTranslated = false),
        ),
        isPatch: Boolean = false,
    ) = Release(
        id = id,
        releaseDate = released,
        isPatch = isPatch,
        languages = languages,
        platforms = listOf(Platform("win")),
        novels = listOf(
            ReleaseNovel(
                id = novelId,
                title = "Novel $novelId",
                originalLanguage = originalLanguage,
                releaseDate = novelReleased,
                rating = 81.2,
                cover = safeCover,
            ),
        ),
    )
}
