package com.zc.vndb.feature.detail.presentation.component.detailhero

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zc.vndb.R
import com.zc.vndb.common.ui.component.chip.TagChipView
import com.zc.vndb.common.ui.component.coverimage.CoverImageView
import com.zc.vndb.common.ui.component.ratingbadge.RatingBadgeView
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.detail.presentation.component.detailhero.model.DetailHeroUIModel
import com.zc.vndb.feature.detail.presentation.component.sectioncard.SectionCardView
import kotlinx.collections.immutable.persistentListOf

private val CoverWidth = 128.dp
private val MetaLabelWidth = 72.dp

/** Cover with score, titles, top tags, age rating, and developer / release / length lines. */
@Composable
fun DetailHeroView(model: DetailHeroUIModel, modifier: Modifier = Modifier) {
    SectionCardView(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.md)) {
            Box(modifier = Modifier.width(CoverWidth)) {
                CoverImageView(
                    url = model.coverUrl,
                    isSensitive = model.isCoverSensitive,
                    contentDescription = model.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(VndbTheme.dimens.coverAspectRatio),
                )
                model.ratingLabel?.let { rating ->
                    RatingBadgeView(
                        label = rating,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(VndbTheme.spacing.xs),
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
            ) {
                Text(
                    text = model.title.asString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                model.originalTitle?.let { original ->
                    Text(
                        text = original.asString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                HeroChips(model)
            }
        }
        MetaLine(R.string.detail_dev, model.developerLabel)
        MetaLine(R.string.detail_released, model.releasedLabel)
        LengthLine(model)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeroChips(model: DetailHeroUIModel) {
    FlowRow(
        modifier = Modifier.padding(top = VndbTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
    ) {
        model.tags.forEach { tag -> TagChipView(label = tag) }
        model.ageLabel?.let { age ->
            TagChipView(
                label = age,
                colors = VndbTheme.colors.spoilerNone,
            )
        }
    }
}

@Composable
private fun MetaLine(label: StringResource, value: UiText?) {
    if (value == null) return
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(MetaLabelWidth),
        )
        Text(
            text = value.asString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun LengthLine(model: DetailHeroUIModel) {
    val length = model.lengthLabel ?: return
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.detail_length),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(MetaLabelWidth),
        )
        Text(
            text = length.asString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        model.averagePlayLabel?.let { average ->
            Text(
                text = " ${stringResource(R.string.separator_dot)} ${average.asString()}",
                style = MaterialTheme.typography.labelMedium,
                color = VndbTheme.colors.accent,
            )
        }
    }
}

@AdaptivePreviews
@Composable
private fun DetailHeroViewPreview() {
    VndbTheme {
        DetailHeroView(
            model = DetailHeroUIModel(
                title = UiText.StringValue("Steins;Gate"),
                originalTitle = UiText.StringValue("シュタインズ・ゲート"),
                coverUrl = null,
                isCoverSensitive = false,
                ratingLabel = UiText.StringValue("9.02"),
                developerLabel = UiText.StringValue("5pb. / Nitroplus"),
                releasedLabel = UiText.StringValue("2009-10-15"),
                lengthLabel = UiText.StringValue("30-50h"),
                averagePlayLabel = UiText.StringValue("44h avg"),
                ageLabel = UiText.StringValue("All Ages / 15+"),
                tags = persistentListOf(
                    UiText.StringValue("Sci-Fi"),
                    UiText.StringValue("Time Travel"),
                ),
            ),
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        )
    }
}
