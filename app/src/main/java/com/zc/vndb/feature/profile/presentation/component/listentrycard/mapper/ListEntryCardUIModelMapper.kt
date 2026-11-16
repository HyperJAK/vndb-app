package com.zc.vndb.feature.profile.presentation.component.listentrycard.mapper

import com.zc.vndb.common.domain.model.TitleLanguage
import com.zc.vndb.common.ui.component.entrylabel.EntryLabelUIModel
import com.zc.vndb.common.ui.mapper.primaryTitle
import com.zc.vndb.common.ui.resource.PluralResource
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.profile.domain.model.ListEntry
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.domain.model.MyListEntry
import com.zc.vndb.feature.profile.presentation.component.listentrycard.model.ListEntryCardUIModel
import kotlin.time.Duration
import kotlin.time.Instant
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val HOURS_PER_DAY = 24
private const val DAYS_SHOWN_AS_AGO = 30
private const val VOTE_SCALE = 10.0

/**
 * A list entry as a card. The label being shown ([shownLabelId]) and "Voted" (the vote is shown
 * instead) are left out of its labels.
 */
fun ListEntry.toCardUIModel(
    shownLabelId: Int,
    now: Instant,
    titleLanguage: TitleLanguage = TitleLanguage.Romanized,
): ListEntryCardUIModel = toCardUIModel(
    shownLabelId,
    now,
    primaryTitle(title, originalTitle, titleLanguage),
)

private fun ListEntry.toCardUIModel(shownLabelId: Int, now: Instant, primaryTitle: UiText) =
    ListEntryCardUIModel(
        novelId = novelId,
        title = primaryTitle,
        coverUrl = cover?.thumbnailUrl ?: cover?.url,
        isCoverSensitive = cover?.isSensitive ?: false,
        voteLabel = vote?.let {
            UiText.StringResource(
                StringResource.profile_my_vote,
                listOf("%.1f".format(it / VOTE_SCALE)),
            )
        },
        dates = listOfNotNull(
            started?.let {
                UiText.StringResource(
                    StringResource.profile_started,
                    listOf(it.toString()),
                )
            },
            finished?.let {
                UiText.StringResource(
                    StringResource.profile_finished,
                    listOf(it.toString()),
                )
            },
        ).toImmutableList(),
        labels = labels
            .filter { it.id != shownLabelId && it.id != ListLabel.VOTED }
            .sortedBy { it.id !in MyListEntry.STATUS_LABELS }
            .map { EntryLabelUIModel(it.id, UiText.StringValue(it.name)) }
            .toImmutableList(),
        notes = notes?.let(UiText::StringValue),
        updatedLabel = lastModified.toUpdatedLabel(now),
    )

/** "Updated just now", "… 5 minutes / 3 hours / 12 days ago", then the date. */
fun Instant.toUpdatedLabel(now: Instant): UiText {
    val elapsed: Duration = now - this
    val minutes = elapsed.inWholeMinutes.toInt()
    val hours = elapsed.inWholeHours.toInt()
    val days = elapsed.inWholeDays.toInt()
    return when {
        minutes < 1 -> UiText.StringResource(StringResource.profile_updated_just_now)

        hours < 1 -> UiText.StringPlural(
            PluralResource.profile_updated_minutes,
            minutes,
            listOf(minutes),
        )

        hours < HOURS_PER_DAY -> UiText.StringPlural(
            PluralResource.profile_updated_hours,
            hours,
            listOf(hours),
        )

        days < DAYS_SHOWN_AS_AGO -> UiText.StringPlural(
            PluralResource.profile_updated_days,
            days,
            listOf(days),
        )

        else -> UiText.StringResource(
            StringResource.profile_updated_on,
            listOf(toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()),
        )
    }
}
