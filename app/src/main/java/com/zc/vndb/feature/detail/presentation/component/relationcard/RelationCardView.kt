package com.zc.vndb.feature.detail.presentation.component.relationcard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.component.chip.TagChipView
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.detail.presentation.component.relationcard.model.RelationCardUIModel

private val CardWidth = 196.dp
private const val TITLE_MAX_LINES = 2

/** A related VN: id, relation, title, year and score. */
@Composable
fun RelationCardView(
    model: RelationCardUIModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(CardWidth)
            .clip(MaterialTheme.shapes.medium)
            .background(VndbTheme.colors.surfaceInset)
            .clickable(onClick = onClick)
            .padding(VndbTheme.spacing.sm),
        verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TagChipView(label = UiText.StringValue(model.id))
            Spacer(Modifier.weight(1f))
            Text(
                text = model.relationLabel.asString(),
                style = MaterialTheme.typography.labelSmall,
                color = if (model.isOfficial) VndbTheme.colors.score else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
        Text(
            text = model.title.asString(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = TITLE_MAX_LINES,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = listOfNotNull(model.yearLabel?.asString(), model.scoreLabel?.asString())
                .joinToString(
                    " ${UiText.StringResource(StringResource.separator_dot).asString()} ",
                ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@AdaptivePreviews
@Composable
private fun RelationCardViewPreview() {
    VndbTheme {
        RelationCardView(
            model = RelationCardUIModel(
                id = "v17102",
                title = UiText.StringValue("Steins;Gate 0"),
                relationLabel = UiText.StringValue("SIDE STORY"),
                isOfficial = true,
                yearLabel = UiText.StringValue("2015"),
                scoreLabel = UiText.StringValue("Score: 8.08"),
            ),
            onClick = {},
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        )
    }
}
