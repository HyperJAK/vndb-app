package com.zc.vndb.common.ui.component.linksrow

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.component.linksrow.model.LinkUIModel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.theme.cardBackground
import com.zc.vndb.common.ui.util.AdaptivePreviews
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val IconSize = 14.dp

/** Horizontally scrolling link pills (vndb.org first). */
@Composable
fun LinksRowView(
    links: ImmutableList<LinkUIModel>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
    ) {
        items(
            items = links,
            key = { it.url },
        ) { link -> LinkPill(link = link, onClick = { onLinkClick(link.url) }) }
    }
}

@Composable
private fun LinkPill(link: LinkUIModel, onClick: () -> Unit) {
    val shape = VndbTheme.shapes.pill
    Row(
        modifier = Modifier
            .heightIn(min = VndbTheme.dimens.filterChipHeight)
            .clip(shape)
            .cardBackground()
            .border(VndbTheme.dimens.borderWidth, VndbTheme.colors.borderSubtle, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = VndbTheme.spacing.md, vertical = VndbTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (link.isVndb) Icons.Outlined.Link else Icons.Outlined.Public,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(IconSize),
        )
        Text(
            text = link.label.asString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@AdaptivePreviews
@Composable
private fun LinksRowViewPreview() {
    VndbTheme {
        LinksRowView(
            links = persistentListOf(
                LinkUIModel(
                    UiText.StringValue("vndb.org/v2002"),
                    "https://vndb.org/v2002",
                    isVndb = true,
                ),
                LinkUIModel(
                    UiText.StringValue("Wikipedia (en)"),
                    "https://en.wikipedia.org",
                    isVndb = false,
                ),
            ),
            onLinkClick = {},
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        )
    }
}
