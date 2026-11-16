package com.zc.vndb.feature.profile.presentation.component.listentrycard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.component.chip.TagChipView
import com.zc.vndb.common.ui.component.coverimage.CoverImageView
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.profile.presentation.component.listentrycard.model.ListEntryCardUIModel
import kotlinx.collections.immutable.persistentListOf

private val CoverWidth = 84.dp
private val SmallIconSize = 14.dp
private const val NOTES_MAX_LINES = 2

/** A VN on the user's list: cover, titles, vote, dates, other labels, notes and last change. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListEntryCardView(
    model: ListEntryCardUIModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = MaterialTheme.shapes.large
    Row(
        modifier = modifier
            .clip(shape)
            .background(VndbTheme.colors.surfaceCard)
            .border(VndbTheme.dimens.borderWidth, VndbTheme.colors.borderSubtle, shape)
            .clickable(onClick = onClick)
            .padding(VndbTheme.spacing.md),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.md),
    ) {
        CoverImageView(
            url = model.coverUrl,
            isSensitive = model.isCoverSensitive,
            contentDescription = model.title,
            modifier = Modifier
                .width(CoverWidth)
                .aspectRatio(VndbTheme.dimens.coverAspectRatio)
                .clip(MaterialTheme.shapes.small),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        ) {
            EntryTitles(model)
            EntryMeta(model)
            model.notes?.let { notes ->
                Text(
                    text = "“${notes.asString()}”",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = NOTES_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            UpdatedLine(model.updatedLabel)
        }
    }
}

@Composable
private fun EntryTitles(model: ListEntryCardUIModel) {
    Column(verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs)) {
        Text(
            text = model.title.asString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        model.originalTitle?.let { original ->
            Text(
                text = original.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun UpdatedLine(label: UiText) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Outlined.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(SmallIconSize),
        )
        Text(
            text = label.asString(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Vote, start/finish dates and the entry's other labels, wrapping as needed. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EntryMeta(model: ListEntryCardUIModel) {
    if (model.voteLabel == null && model.dates.isEmpty() && model.labels.isEmpty()) return
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
    ) {
        model.voteLabel?.let { TagChipView(label = it, colors = VndbTheme.colors.spoilerMinor) }
        model.dates.forEach { TagChipView(label = it) }
        model.labels.forEach { TagChipView(label = it, colors = VndbTheme.colors.chipLength) }
    }
}

@AdaptivePreviews
@Composable
private fun ListEntryCardViewPreview() {
    VndbTheme {
        ListEntryCardView(
            model = ListEntryCardUIModel(
                novelId = "v13631",
                title = UiText.StringValue("Hello Lady!"),
                originalTitle = UiText.StringValue("ハロー・レディ！"),
                coverUrl = null,
                isCoverSensitive = false,
                voteLabel = UiText.StringValue("My vote 8.5"),
                dates = persistentListOf(UiText.StringValue("Started 2024-10-15")),
                labels = persistentListOf(UiText.StringValue("Waiting")),
                notes = UiText.StringValue("Currently on Sorako's route."),
                updatedLabel = UiText.StringValue("Updated 2 hours ago"),
            ),
            onClick = {},
        )
    }
}
