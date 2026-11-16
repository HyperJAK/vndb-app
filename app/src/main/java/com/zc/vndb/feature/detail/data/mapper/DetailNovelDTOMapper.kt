package com.zc.vndb.feature.detail.data.mapper

import com.zc.vndb.common.data.mapper.toCover
import com.zc.vndb.common.data.mapper.toDomain
import com.zc.vndb.common.data.mapper.toNovelLength
import com.zc.vndb.common.data.mapper.toReleaseDate
import com.zc.vndb.feature.detail.data.dto.DetailNovelDTO
import com.zc.vndb.feature.detail.data.dto.RelationDTO
import com.zc.vndb.feature.detail.domain.model.ExternalLink
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.domain.model.RelatedNovel
import com.zc.vndb.feature.detail.domain.model.RelationType

fun DetailNovelDTO.toDomain(): NovelDetail = NovelDetail(
    id = id,
    title = title.orEmpty(),
    originalTitle = alttitle?.takeIf { it.isNotBlank() && it != title },
    releaseDate = released.toReleaseDate(),
    rating = rating,
    average = average,
    voteCount = votecount ?: 0,
    cover = image?.toCover(),
    developerNames = developers.orEmpty().map { it.name },
    description = description?.takeIf { it.isNotBlank() },
    length = toNovelLength(lengthMinutes = lengthMinutes, estimate = length),
    averagePlayMinutes = lengthMinutes,
    tags = tags.orEmpty().mapNotNull { it.toDomain() },
    links = extlinks.orEmpty().map { ExternalLink(label = it.label, url = it.url) },
    relations = relations.orEmpty().map { it.toDomain() },
    voiceActors = va.orEmpty()
        .groupBy { it.character.id }
        .mapValues { (_, actors) -> actors.first().staff.name },
)

fun RelationDTO.toDomain(): RelatedNovel = RelatedNovel(
    id = id,
    title = title.orEmpty(),
    relation = relation.toRelationType(),
    isOfficial = relationOfficial ?: false,
    releaseDate = released.toReleaseDate(),
    rating = rating,
)

private fun String?.toRelationType(): RelationType = when (this) {
    "seq" -> RelationType.Sequel
    "preq" -> RelationType.Prequel
    "set" -> RelationType.SameSetting
    "alt" -> RelationType.AlternativeVersion
    "char" -> RelationType.SharesCharacters
    "side" -> RelationType.SideStory
    "par" -> RelationType.ParentStory
    "ser" -> RelationType.SameSeries
    "fan" -> RelationType.FanDisc
    "orig" -> RelationType.OriginalGame
    else -> RelationType.Other
}
