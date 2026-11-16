package com.zc.vndb.feature.detail.domain.model

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.Cover
import com.zc.vndb.common.domain.model.NovelLength
import com.zc.vndb.common.domain.model.NovelTag
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.domain.model.TagCategory
import kotlinx.datetime.LocalDate

object DetailSamples {

    fun date(year: Int, month: Int = 1, day: Int = 1): ReleaseDate = ReleaseDate.Full(
        LocalDate(year, month, day),
    )

    fun tag(
        name: String,
        rating: Double = 2.0,
        spoiler: Int = 0,
        category: TagCategory = TagCategory.Content,
    ) = NovelTag(name = name, rating = rating, spoilerLevel = spoiler, category = category)

    fun novel(
        id: String = "v2002",
        tags: List<NovelTag> = emptyList(),
        relations: List<RelatedNovel> = emptyList(),
        links: List<ExternalLink> = emptyList(),
        description: String? = "Hello",
        cover: Cover? = Cover(url = "full", thumbnailUrl = "thumb", sexual = 0.0, violence = 0.0),
        voiceActors: Map<String, String> = emptyMap(),
    ) = NovelDetail(
        id = id,
        title = "STEINS;GATE",
        originalTitle = null,
        releaseDate = date(2009, 10, 15),
        rating = 90.2,
        average = 90.25,
        voteCount = 17205,
        cover = cover,
        developerNames = listOf("NITRO PLUS", "MAGES.", "Third"),
        description = description,
        length = NovelLength.Long,
        averagePlayMinutes = 2625,
        tags = tags,
        links = links,
        relations = relations,
        voiceActors = voiceActors,
    )

    fun relation(
        id: String,
        official: Boolean = true,
        released: ReleaseDate = date(
            2015,
        ),
    ) = RelatedNovel(
        id = id,
        title = "Novel $id",
        relation = RelationType.SideStory,
        isOfficial = official,
        releaseDate = released,
        rating = 80.8,
    )

    fun character(id: String, role: CharacterRole = CharacterRole.Primary, spoiler: Int = 0) =
        NovelCharacter(
            id = id,
            name = "Character $id",
            originalName = null,
            image = null,
            role = role,
            spoilerLevel = spoiler,
            voiceActor = null,
        )

    fun edition(
        id: String,
        platform: String = "win",
        released: ReleaseDate = date(2010),
        official: Boolean = true,
        patch: Boolean = false,
        minAge: Int? = 15,
    ) = Edition(
        id = id,
        title = "Edition $id",
        releaseDate = released,
        platform = platform,
        family = PlatformFamily.Pc,
        languageCodes = listOf("ja"),
        isOfficial = official,
        isPatch = patch,
        minAge = minAge,
    )
}
