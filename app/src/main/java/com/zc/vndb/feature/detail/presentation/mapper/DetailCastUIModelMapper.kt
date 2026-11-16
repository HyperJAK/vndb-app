package com.zc.vndb.feature.detail.presentation.mapper

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.ui.component.factline.model.FactUIModel
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.resource.PluralResource
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.NovelCast
import com.zc.vndb.feature.detail.domain.model.StaffCredit
import com.zc.vndb.feature.detail.domain.model.StaffCreditRole
import com.zc.vndb.feature.detail.presentation.component.charactercard.mapper.toCardUIModel
import com.zc.vndb.feature.detail.presentation.model.CastUIModel
import com.zc.vndb.feature.detail.presentation.model.CharacterGroupUIModel
import com.zc.vndb.feature.detail.presentation.model.StaffGroupUIModel
import kotlinx.collections.immutable.toImmutableList

private const val PREVIEW_SIZE = 12

/** Characters [level] allows, grouped by role, and staff grouped by edition (original first). */
fun NovelCast.toCastUIModel(level: SpoilerLevel): CastUIModel {
    val visible = characters.filter { it.spoilerLevel <= level.maxLevel }
    val major = visible.filter { it.role == CharacterRole.Main || it.role == CharacterRole.Primary }
    val hidden = characters.size - visible.size
    return CastUIModel(
        preview = major.take(PREVIEW_SIZE).map { it.toCardUIModel() }.toImmutableList(),
        mainCountLabel = major.size.takeIf { it > 0 }
            ?.let { UiText.StringPlural(PluralResource.detail_main_characters, it, listOf(it)) },
        characterGroups = visible.groupBy { it.role }.toSortedMap().map { (role, members) ->
            CharacterGroupUIModel(
                title = role.toGroupLabel(),
                characters = members.map { it.toCardUIModel() }.toImmutableList(),
            )
        }.toImmutableList(),
        staffGroups = staffGroups().toImmutableList(),
        hiddenLabel = hidden.takeIf { it > 0 }
            ?.let { UiText.StringPlural(PluralResource.detail_hidden_by_spoiler, it, listOf(it)) },
        characterCount = characters.size,
    )
}

private fun NovelCast.staffGroups(): List<StaffGroupUIModel> {
    val byEdition = staff.groupBy { it.editionId }
    val original = byEdition[null]?.let {
        StaffGroupUIModel(
            UiText.StringResource(StringResource.detail_staff_original),
            it.toFacts(),
        )
    }
    val others = editions.mapNotNull { edition ->
        byEdition[edition.id]?.let {
            StaffGroupUIModel(
                UiText.StringValue(edition.name),
                it.toFacts(),
            )
        }
    }
    return listOfNotNull(original) + others
}

/** One line per role ("Scenario: Hino Yuuki, Hayase Ryou"), in VNDB's role order. */
private fun List<StaffCredit>.toFacts() = groupBy { it.role }.toSortedMap().map { (role, credits) ->
    FactUIModel(
        label = role.toLabel(),
        value = UiText.StringValue(
            credits.joinToString(
                ", ",
            ) { credit -> credit.note?.let { "${credit.name} ($it)" } ?: credit.name },
        ),
    )
}.toImmutableList()

private fun CharacterRole.toGroupLabel(): UiText = UiText.StringResource(
    when (this) {
        CharacterRole.Main -> StringResource.detail_characters_main
        CharacterRole.Primary -> StringResource.detail_characters_primary
        CharacterRole.Side -> StringResource.detail_characters_side
        CharacterRole.Appears -> StringResource.detail_characters_appears
    },
)

private fun StaffCreditRole.toLabel(): UiText = UiText.StringResource(
    when (this) {
        StaffCreditRole.Scenario -> StringResource.detail_staff_scenario
        StaffCreditRole.CharacterDesign -> StringResource.detail_staff_chardesign
        StaffCreditRole.Art -> StringResource.detail_staff_art
        StaffCreditRole.Music -> StringResource.detail_staff_music
        StaffCreditRole.Songs -> StringResource.detail_staff_songs
        StaffCreditRole.Director -> StringResource.detail_staff_director
        StaffCreditRole.Translator -> StringResource.detail_staff_translator
        StaffCreditRole.Editor -> StringResource.detail_staff_editor
        StaffCreditRole.QualityAssurance -> StringResource.detail_staff_qa
        StaffCreditRole.Staff -> StringResource.detail_staff_staff
        StaffCreditRole.Other -> StringResource.detail_staff_other
    },
)
