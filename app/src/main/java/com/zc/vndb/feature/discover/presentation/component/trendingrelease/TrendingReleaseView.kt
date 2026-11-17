package com.zc.vndb.feature.discover.presentation.component.trendingrelease

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.component.chip.TagChipView
import com.zc.vndb.common.ui.component.coverimage.CoverImageView
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.TintedColors
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.theme.cardBackground
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.discover.presentation.component.trendingrelease.model.ReleaseKindTone
import com.zc.vndb.feature.discover.presentation.component.trendingrelease.model.TrendingReleaseUIModel
import kotlinx.collections.immutable.persistentListOf

private val ThumbnailWidth = 56.dp
private val StarSize = 12.dp

/** A "New & Trending" row: thumbnail, release label and date, title, platforms and score. */
@Composable
fun TrendingReleaseView(
    model: TrendingReleaseUIModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = MaterialTheme.shapes.medium
    Row(
        modifier = modifier
            .clip(shape)
            .cardBackground()
            .border(VndbTheme.dimens.borderWidth, VndbTheme.colors.borderSubtle, shape)
            .clickable(onClick = onClick)
            .padding(VndbTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoverImageView(
            url = model.thumbnailUrl,
            isSensitive = model.isCoverSensitive,
            contentDescription = model.title,
            modifier = Modifier
                .width(ThumbnailWidth)
                .aspectRatio(VndbTheme.dimens.coverAspectRatio),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        ) {
            ReleaseLabelRow(model)
            Text(
                text = model.title.asString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                model.platforms.forEach { platform -> TagChipView(label = platform) }
                Spacer(Modifier.weight(1f))
                model.ratingLabel?.let { rating -> RatingText(rating) }
            }
        }
    }
}

@Composable
private fun ReleaseLabelRow(model: TrendingReleaseUIModel) {
    Row(horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs)) {
        Text(
            text = model.kindLabel.asString(),
            style = MaterialTheme.typography.labelSmall,
            color = model.kindTone.colors().content,
        )
        Text(
            text = UiText.StringResource(StringResource.separator_dot).asString(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = model.dateLabel.asString(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun RatingText(rating: UiText) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = VndbTheme.colors.score,
            modifier = Modifier.size(StarSize),
        )
        Text(
            text = rating.asString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ReleaseKindTone.colors(): TintedColors = when (this) {
    ReleaseKindTone.New -> VndbTheme.colors.releaseNew
    ReleaseKindTone.Translation -> VndbTheme.colors.releaseTranslation
    ReleaseKindTone.Patch -> VndbTheme.colors.releasePatch
    ReleaseKindTone.Rerelease -> VndbTheme.colors.releaseRerelease
}

@AdaptivePreviews
@Composable
private fun TrendingReleaseViewPreview() {
    VndbTheme {
        TrendingReleaseView(
            model = TrendingReleaseUIModel(
                releaseId = "r1",
                novelId = "v1",
                title = UiText.StringValue("ATRI -My Dear Moments-"),
                kindLabel = UiText.StringValue("NEW TRANSLATION"),
                kindTone = ReleaseKindTone.Translation,
                dateLabel = UiText.StringValue("Oct 2024"),
                ratingLabel = UiText.StringValue("8.12"),
                thumbnailUrl = null,
                isCoverSensitive = false,
                platforms = persistentListOf(
                    UiText.StringValue("Windows"),
                    UiText.StringValue("Switch"),
                ),
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(VndbTheme.spacing.lg),
        )
    }
}
