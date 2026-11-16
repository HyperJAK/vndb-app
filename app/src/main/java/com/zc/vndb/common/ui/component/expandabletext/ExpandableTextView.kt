package com.zc.vndb.common.ui.component.expandabletext

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zc.vndb.R
import com.zc.vndb.common.ui.component.sectioncard.SectionCardView
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private const val COLLAPSED_LINES = 4
private val ArrowSize = 16.dp

/** A titled card of long text, collapsed to a few lines with "Read more" when it does not fit. */
@Composable
fun ExpandableTextView(
    title: UiText,
    text: UiText,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Only offer "Read more" when the collapsed text is actually cut off (purely visual state).
    var isTruncated by remember(text) { mutableStateOf(false) }
    SectionCardView(modifier = modifier, title = title) {
        Text(
            text = text.asString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = if (isExpanded) Int.MAX_VALUE else COLLAPSED_LINES,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { layout -> if (!isExpanded) isTruncated = layout.hasVisualOverflow },
            modifier = Modifier.animateContentSize(),
        )
        if (isExpanded || isTruncated) {
            ReadMoreButton(isExpanded = isExpanded, onToggle = onToggle)
        }
    }
}

@Composable
private fun ReadMoreButton(isExpanded: Boolean, onToggle: () -> Unit) {
    TextButton(onClick = onToggle) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(
                    if (isExpanded) R.string.detail_read_less else R.string.detail_read_more,
                ),
                style = MaterialTheme.typography.labelLarge,
                color = VndbTheme.colors.accent,
            )
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = VndbTheme.colors.accent,
                modifier = Modifier.size(ArrowSize),
            )
        }
    }
}

@AdaptivePreviews
@Composable
private fun ExpandableTextViewPreview() {
    VndbTheme {
        ExpandableTextView(
            title = UiText.StringValue("Synopsis"),
            text = UiText.StringValue(
                "Set in Akihabara in summer 2010, self-proclaimed mad scientist Rintaro Okabe…",
            ),
            isExpanded = false,
            onToggle = {},
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        )
    }
}
