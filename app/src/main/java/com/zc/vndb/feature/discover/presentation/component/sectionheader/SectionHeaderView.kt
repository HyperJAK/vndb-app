package com.zc.vndb.feature.discover.presentation.component.sectionheader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val IconSize = 18.dp

/** Section title with a leading accent icon and an optional trailing action. */
@Composable
fun SectionHeaderView(
    title: UiText,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = VndbTheme.colors.accent,
            modifier = Modifier.size(IconSize),
        )
        Text(
            text = title.asString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.weight(1f))
        action?.invoke()
    }
}

@AdaptivePreviews
@Composable
private fun SectionHeaderViewPreview() {
    VndbTheme {
        SectionHeaderView(
            title = UiText.StringValue("Top Rated"),
            icon = Icons.Outlined.EmojiEvents,
            action = { Text("See all", color = VndbTheme.colors.accent) },
        )
    }
}
