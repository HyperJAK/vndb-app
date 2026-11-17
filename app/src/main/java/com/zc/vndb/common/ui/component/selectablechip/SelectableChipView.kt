package com.zc.vndb.common.ui.component.selectablechip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.TintedColors
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

/** Compact filter chip (28dp, DESIGN.md "Filter Chips"). [leading] adds a mark before
 * the label. [selectedColors] styles the selected state. */
@Composable
fun SelectableChipView(
    label: UiText,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    selectedColors: TintedColors = SelectableChipDefaults.colors(),
) {
    val colors = if (isSelected) {
        selectedColors
    } else {
        TintedColors(
            container = VndbTheme.colors.chipInactiveContainer,
            content = MaterialTheme.colorScheme.onSurfaceVariant,
            border = VndbTheme.colors.borderSubtle,
        )
    }
    val shape = VndbTheme.shapes.pill
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        modifier = modifier
            .heightIn(min = VndbTheme.dimens.filterChipHeight)
            .clip(shape)
            .background(colors.container)
            .border(VndbTheme.dimens.borderWidth, colors.border, shape)
            .clickable(role = Role.Tab, onClick = onClick)
            .semantics { selected = isSelected }
            .padding(horizontal = VndbTheme.spacing.md, vertical = VndbTheme.spacing.xs)
            .wrapContentHeight(),
    ) {
        leading?.invoke()
        Text(
            text = label.asString(),
            style = MaterialTheme.typography.labelLarge,
            color = colors.content,
            maxLines = 1,
        )
    }
}

@AdaptivePreviews
@Composable
private fun SelectableChipViewPreview() {
    VndbTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
            modifier = Modifier.padding(VndbTheme.spacing.sm),
        ) {
            SelectableChipView(
                label = UiText.StringValue("Romance"),
                isSelected = true,
                onClick = {},
            )
            SelectableChipView(
                label = UiText.StringValue("Mystery"),
                isSelected = false,
                onClick = {},
            )
        }
    }
}
