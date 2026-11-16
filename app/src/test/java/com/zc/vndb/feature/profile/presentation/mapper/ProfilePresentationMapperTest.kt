package com.zc.vndb.feature.profile.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.profile.domain.model.EntryLabel
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.domain.model.ProfileSamples
import com.zc.vndb.feature.profile.domain.model.UserProfile
import com.zc.vndb.feature.profile.presentation.component.listentrycard.mapper.toCardUIModel
import com.zc.vndb.feature.profile.presentation.component.listentrycard.mapper.toUpdatedLabel
import com.zc.vndb.feature.profile.presentation.component.profileheader.mapper.toHeaderUIModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class ProfilePresentationMapperTest {

    private val now = Instant.parse("2026-11-16T12:00:00Z")

    @Test
    fun `picks the current label, else Playing, else the first`() {
        val labels = listOf(ProfileSamples.label(2), ProfileSamples.label(ListLabel.PLAYING))
        assertEquals(2, labels.pickSelected(current = 2))
        assertEquals(ListLabel.PLAYING, labels.pickSelected(current = 99))
        assertEquals(2, listOf(ProfileSamples.label(2)).pickSelected(current = null))
        assertNull(emptyList<ListLabel>().pickSelected(current = 1))
    }

    @Test
    fun `chips show counts and the selection`() {
        val chips = listOf(
            ProfileSamples.label(1, count = 1200, name = "Playing"),
        ).toChips(selectedId = 1)

        assertEquals(
            UiText.StringResource(R.string.profile_label_with_count, listOf("Playing", "1,200")),
            chips.single().label,
        )
        assertEquals(true, chips.single().isSelected)
    }

    @Test
    fun `header shows play time and flags hidden private lists`() {
        val header = ProfileSamples.session(canReadPrivateList = false)
            .toHeaderUIModel(
                UserProfile("u2", "Yorhel", lengthVotes = 10, lengthVotesMinutes = 10_239),
            )

        assertEquals(UiText.StringValue("Y"), header.initial)
        assertEquals(
            UiText.StringResource(
                R.string.profile_stats_line,
                listOf(
                    UiText.StringPlural(R.plurals.profile_length_votes, 10, listOf("10")),
                    UiText.StringResource(R.string.profile_hours_logged, listOf("171")),
                ),
            ),
            header.statsLabel,
        )
        assertNotNull(header.privateNote)
        assertNull(ProfileSamples.session().toHeaderUIModel().privateNote)
    }

    @Test
    fun `cards leave out the shown label and Voted, and show the vote`() {
        val card = ProfileSamples.entry(
            "v1",
            labels = listOf(
                EntryLabel(ListLabel.PLAYING, "Playing"),
                EntryLabel(ListLabel.VOTED, "Voted"),
                EntryLabel(13, "Waiting"),
            ),
            vote = 85,
        ).toCardUIModel(shownLabelId = ListLabel.PLAYING, now = now)

        assertEquals(listOf<UiText>(UiText.StringValue("Waiting")), card.labels.toList())
        assertEquals(UiText.StringResource(R.string.profile_my_vote, listOf("8.5")), card.voteLabel)
        assertEquals(
            listOf<UiText>(UiText.StringResource(R.string.profile_started, listOf("2026-01-07"))),
            card.dates.toList(),
        )
    }

    @Test
    fun `updated label goes from just now to minutes, hours, days and a date`() {
        assertEquals(
            UiText.StringResource(R.string.profile_updated_just_now),
            (now - 30.seconds).toUpdatedLabel(now),
        )
        assertEquals(
            UiText.StringPlural(R.plurals.profile_updated_minutes, 5, listOf(5)),
            (now - 5.minutes).toUpdatedLabel(now),
        )
        assertEquals(
            UiText.StringPlural(R.plurals.profile_updated_hours, 3, listOf(3)),
            (now - 3.hours).toUpdatedLabel(now),
        )
        assertEquals(
            UiText.StringPlural(R.plurals.profile_updated_days, 2, listOf(2)),
            (now - 2.days).toUpdatedLabel(now),
        )
        assertEquals(
            R.string.profile_updated_on,
            (now - 60.days).toUpdatedLabel(now).let { (it as UiText.StringResource).id },
        )
    }
}
