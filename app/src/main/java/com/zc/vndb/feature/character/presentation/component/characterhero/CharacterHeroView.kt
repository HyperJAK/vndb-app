package com.zc.vndb.feature.character.presentation.component.characterhero

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zc.vndb.R
import com.zc.vndb.common.ui.component.chip.TagChipView
import com.zc.vndb.common.ui.component.coverimage.CoverImageView
import com.zc.vndb.common.ui.component.sectioncard.SectionCardView
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.character.presentation.component.characterhero.model.CharacterFactUIModel
import com.zc.vndb.feature.character.presentation.component.characterhero.model.CharacterHeroUIModel
import kotlinx.collections.immutable.persistentListOf

private val PortraitWidth = 128.dp
private val FactLabelWidth = 88.dp

/** Portrait, names, sex / gender chips and profile facts. */
@Composable
fun CharacterHeroView(model: CharacterHeroUIModel, modifier: Modifier = Modifier) {
    SectionCardView(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.md)) {
            CoverImageView(
                url = model.imageUrl,
                isSensitive = model.isImageSensitive,
                contentDescription = model.name,
                modifier = Modifier
                    .width(PortraitWidth)
                    .aspectRatio(VndbTheme.dimens.coverAspectRatio),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
            ) {
                Text(
                    text = model.name.asString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                model.originalName?.let { original ->
                    Text(
                        text = original.asString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IdentityChips(model)
                model.facts.forEach { fact -> FactLine(fact) }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IdentityChips(model: CharacterHeroUIModel) {
    if (model.identity.isEmpty() && model.spoilerIdentity.isEmpty()) return
    FlowRow(
        modifier = Modifier.padding(vertical = VndbTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
    ) {
        model.identity.forEach { label -> TagChipView(label = label) }
        model.spoilerIdentity.forEach { label ->
            TagChipView(
                label = label,
                colors = VndbTheme.colors.spoilerMajor,
            )
        }
    }
}

@Composable
private fun FactLine(fact: CharacterFactUIModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(fact.label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(FactLabelWidth),
        )
        Text(
            text = fact.value.asString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@AdaptivePreviews
@Composable
private fun CharacterHeroViewPreview() {
    VndbTheme {
        CharacterHeroView(
            model = CharacterHeroUIModel(
                name = UiText.StringValue("Makise Kurisu"),
                originalName = UiText.StringValue("牧瀬 紅莉栖"),
                imageUrl = null,
                isImageSensitive = false,
                identity = persistentListOf(UiText.StringValue("Female")),
                facts = persistentListOf(
                    CharacterFactUIModel(R.string.character_age, UiText.StringValue("18")),
                    CharacterFactUIModel(R.string.character_birthday, UiText.StringValue("Jul 25")),
                    CharacterFactUIModel(R.string.character_height, UiText.StringValue("160 cm")),
                ),
            ),
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        )
    }
}
