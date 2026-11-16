package com.zc.vndb.common.ui.component.spoilerfilter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.zc.vndb.R
import com.zc.vndb.common.ui.component.sectioncard.SectionCardView
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val ShieldSize = 16.dp

/** "SPOILER FILTER: None (0) | Minor (1) | Major (2)". */
@Composable
fun SpoilerFilterView(
    level: SpoilerLevel,
    onLevelSelect: (SpoilerLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCardView(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(ShieldSize),
            )
            Text(
                text = stringResource(R.string.detail_spoiler_filter),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.weight(1f))
            SpoilerLevel.entries.forEach { option ->
                LevelOption(
                    label = option.labelRes(),
                    isSelected = option == level,
                    onClick = { onLevelSelect(option) },
                )
            }
        }
    }
}

@Composable
private fun LevelOption(label: StringResource, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = stringResource(label),
        style = MaterialTheme.typography.labelMedium,
        color = if (isSelected) VndbTheme.colors.onAccent else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(if (isSelected) VndbTheme.colors.accent else VndbTheme.colors.surfaceCard)
            .clickable(role = Role.Tab, onClick = onClick)
            .semantics { selected = isSelected }
            .padding(horizontal = VndbTheme.spacing.sm, vertical = VndbTheme.spacing.xs),
    )
}

private fun SpoilerLevel.labelRes(): StringResource = when (this) {
    SpoilerLevel.None -> R.string.detail_spoiler_none
    SpoilerLevel.Minor -> R.string.detail_spoiler_minor
    SpoilerLevel.Major -> R.string.detail_spoiler_major
}

@AdaptivePreviews
@Composable
private fun SpoilerFilterViewPreview() {
    VndbTheme {
        SpoilerFilterView(
            level = SpoilerLevel.None,
            onLevelSelect = {},
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        )
    }
}
