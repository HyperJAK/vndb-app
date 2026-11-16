package com.zc.vndb.common.ui.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.TintedColors
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val HorizontalPadding = 6.dp
private val VerticalPadding = 2.dp

/** Compact mono chip for tags, platforms and short metadata. Neutral unless [colors] is given. */
@Composable
fun TagChipView(
    label: UiText,
    modifier: Modifier = Modifier,
    colors: TintedColors = VndbTheme.colors.chipNeutral,
) {
    val shape = MaterialTheme.shapes.small
    Text(
        text = label.asString(),
        style = MaterialTheme.typography.labelMedium,
        color = colors.content,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .background(colors.container, shape)
            .border(VndbTheme.dimens.borderWidth, colors.border, shape)
            .padding(horizontal = HorizontalPadding, vertical = VerticalPadding),
    )
}

@AdaptivePreviews
@Composable
private fun TagChipViewPreview() {
    VndbTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
            modifier = Modifier.padding(VndbTheme.spacing.sm),
        ) {
            TagChipView(label = UiText.StringValue("Urban Fantasy"))
            TagChipView(label = UiText.StringValue("Windows"))
            TagChipView(
                label = UiText.StringValue("All Ages"),
                colors = VndbTheme.colors.spoilerNone,
            )
        }
    }
}
