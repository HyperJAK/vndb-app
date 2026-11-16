package com.zc.vndb.common.ui.component.sectioncard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

/** A bordered card with an optional title row (title + [trailing]) above [content]. */
@Composable
fun SectionCardView(
    modifier: Modifier = Modifier,
    title: UiText? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = MaterialTheme.shapes.large
    Column(
        modifier = modifier
            .clip(shape)
            .background(VndbTheme.colors.surfaceCard)
            .border(VndbTheme.dimens.borderWidth, VndbTheme.colors.borderSubtle, shape)
            .padding(VndbTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
    ) {
        if (title != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title.asString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.weight(1f))
                trailing?.invoke()
            }
        }
        content()
    }
}

@AdaptivePreviews
@Composable
private fun SectionCardViewPreview() {
    VndbTheme {
        SectionCardView(
            title = UiText.StringValue("Synopsis"),
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        ) {
            Text(
                "Set in Akihabara in summer 2010…",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
