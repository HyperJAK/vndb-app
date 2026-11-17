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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.component.chip.TagChipView
import com.zc.vndb.common.ui.component.coverimage.CoverImageView
import com.zc.vndb.common.ui.component.sectioncard.SectionCardView
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.detail.presentation.component.detailhero.model.DetailHeroUIModel
import kotlinx.collections.immutable.persistentListOf

private val CoverWidth = 128.dp
private val MetaLabelWidth = 72.dp

/** Cover, titles, top tags, age rating, and developer / release / length / score lines. */
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
        MetaLine(UiText.StringResource(StringResource.detail_dev), model.developerLabel)
        MetaLine(UiText.StringResource(StringResource.detail_released), model.releasedLabel)
        LengthLine(model)
        ScoreLine(model)
    }
}

/** "Score:  8.69 / 10 · 12,409 votes", the score in the score color. */
@Composable
private fun ScoreLine(model: DetailHeroUIModel) {
    val score = model.scoreLabel ?: return
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = UiText.StringResource(StringResource.detail_score).asString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(MetaLabelWidth),
        )
        Text(
            text = score.asString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = VndbTheme.colors.score,
        )
        model.scoreVotesLabel?.let { votes ->
            Text(
                text = " ${UiText.StringResource(
                    StringResource.separator_dot,
                ).asString()} ${votes.asString()}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
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
private fun MetaLine(label: UiText, value: UiText?) {
    if (value == null) return
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label.asString(),
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

/** "Length:  [Very Long] (73h) · 90 votes". */
@Composable
private fun LengthLine(model: DetailHeroUIModel) {
    if (model.lengthName == null && model.lengthValueLabel == null) return
    Row(
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = UiText.StringResource(StringResource.detail_length).asString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(MetaLabelWidth - VndbTheme.spacing.sm),
        )
        model.lengthName?.let { TagChipView(label = it, colors = VndbTheme.colors.chipLength) }
        model.lengthValueLabel?.let { value ->
            Text(
                text = value.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        model.lengthVotesLabel?.let { votes ->
            Text(
                text = "${UiText.StringResource(
                    StringResource.separator_dot,
                ).asString()} ${votes.asString()}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                developerLabel = UiText.StringValue("5pb. / Nitroplus"),
                releasedLabel = UiText.StringValue("2009-10-15"),
                lengthName = UiText.StringValue("Long"),
                lengthValueLabel = UiText.StringValue("(44h)"),
                lengthVotesLabel = UiText.StringValue("1,021 length votes"),
                scoreLabel = UiText.StringValue("9.02 / 10"),
                scoreVotesLabel = UiText.StringValue("17,205 votes"),
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
