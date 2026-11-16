package com.zc.vndb.feature.browse.presentation.component.tagheader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zc.vndb.common.ui.component.chip.TagChipView
import com.zc.vndb.common.ui.component.sectioncard.SectionCardView
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.browse.presentation.component.tagheader.model.TagHeaderUIModel

/** The tag a filtered Browse is scoped to: name, category, VNDB count and aliases. */
@Composable
fun TagHeaderView(model: TagHeaderUIModel, modifier: Modifier = Modifier) {
    SectionCardView(modifier = modifier) {
        Text(
            text = model.name.asString(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            model.categoryLabel?.let { category ->
                TagChipView(
                    label = category,
                    colors = if (model.isSexualCategory) VndbTheme.colors.tagNsfw else VndbTheme.colors.chipNeutral,
                )
            }
            Text(
                text = model.countLabel.asString(),
                style = MaterialTheme.typography.labelMedium,
                color = VndbTheme.colors.accent,
            )
        }
        model.aliasesLabel?.let { aliases ->
            Text(
                text = aliases.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@AdaptivePreviews
@Composable
private fun TagHeaderViewPreview() {
    VndbTheme {
        TagHeaderView(
            model = TagHeaderUIModel(
                name = UiText.StringValue("ADV"),
                categoryLabel = UiText.StringValue("Technical tag"),
                isSexualCategory = false,
                countLabel = UiText.StringValue("36,379 visual novels on VNDB"),
                aliasesLabel = UiText.StringValue("Also known as: AVG, Part-screen Textbox"),
                description = null,
            ),
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        )
    }
}
