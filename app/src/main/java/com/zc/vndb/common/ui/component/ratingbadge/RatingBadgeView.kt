package com.zc.vndb.common.ui.component.ratingbadge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val StarSize = 12.dp
private val HorizontalPadding = 6.dp
private val VerticalPadding = 2.dp

/** Score pill: amber star + mono score, on a translucent dark pill (for use over cover art). */
@Composable
fun RatingBadgeView(label: UiText, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(VndbTheme.colors.coverScrim, VndbTheme.shapes.pill)
            .padding(horizontal = HorizontalPadding, vertical = VerticalPadding),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = VndbTheme.colors.score,
            modifier = Modifier.size(StarSize),
        )
        Text(
            text = label.asString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@AdaptivePreviews
@Composable
private fun RatingBadgeViewPreview() {
    VndbTheme {
        RatingBadgeView(
            label = UiText.StringValue("8.92 / 10"),
            modifier = Modifier.padding(VndbTheme.spacing.sm),
        )
    }
}
