package com.zc.vndb.feature.detail.presentation.component.editionrow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.component.chip.TagChipView
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.TintedColors
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.detail.domain.model.PlatformFamily
import com.zc.vndb.feature.detail.presentation.component.editionrow.model.EditionRowUIModel

private val IconBoxSize = 36.dp
private val IconSize = 20.dp

/** One edition: platform icon, title, "Windows • English, Japanese • 2016-09-08", family badge. */
@Composable
fun EditionRowView(model: EditionRowUIModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(VndbTheme.colors.surfaceInset)
            .clickable(onClick = onClick)
            .padding(VndbTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(IconBoxSize)
                .background(VndbTheme.colors.surfaceElevated, MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = model.family.icon(),
                contentDescription = null,
                tint = model.family.colors().content,
                modifier = Modifier.size(IconSize),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = model.title.asString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = details(model),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        TagChipView(label = model.familyLabel, colors = model.family.colors())
    }
}

@Composable
private fun details(model: EditionRowUIModel): String {
    val separator = " ${UiText.StringResource(StringResource.separator_dot).asString()} "
    return listOfNotNull(
        model.platformLabel.asString(),
        model.languagesLabel?.asString(),
        model.dateLabel.asString(),
    )
        .joinToString(separator)
}

private fun PlatformFamily.icon(): ImageVector = when (this) {
    PlatformFamily.Pc -> Icons.Outlined.Computer
    PlatformFamily.Console -> Icons.Outlined.SportsEsports
    PlatformFamily.Mobile -> Icons.Outlined.PhoneAndroid
    PlatformFamily.Web -> Icons.Outlined.Language
    PlatformFamily.Other -> Icons.Outlined.Devices
}

@Composable
private fun PlatformFamily.colors(): TintedColors = when (this) {
    PlatformFamily.Pc -> VndbTheme.colors.releaseTranslation
    PlatformFamily.Console -> VndbTheme.colors.releasePatch
    PlatformFamily.Mobile -> VndbTheme.colors.releaseRerelease
    PlatformFamily.Web, PlatformFamily.Other -> VndbTheme.colors.chipNeutral
}

@AdaptivePreviews
@Composable
private fun EditionRowViewPreview() {
    VndbTheme {
        EditionRowView(
            model = EditionRowUIModel(
                key = "r1-win",
                releaseId = "r1",
                title = UiText.StringValue("Steins;Gate - Steam Edition"),
                platformLabel = UiText.StringValue("Windows"),
                languagesLabel = UiText.StringValue("English, Japanese"),
                dateLabel = UiText.StringValue("2016-09-08"),
                family = PlatformFamily.Pc,
                familyLabel = UiText.StringValue("PC"),
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(VndbTheme.spacing.lg),
        )
    }
}
