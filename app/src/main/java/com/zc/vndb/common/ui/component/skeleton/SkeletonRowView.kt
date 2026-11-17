package com.zc.vndb.common.ui.component.skeleton

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.theme.cardBackground
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val DefaultCoverWidth = 72.dp
private const val TITLE_WIDTH = 0.8f
private const val SUBTITLE_WIDTH = 0.5f
private const val DETAIL_WIDTH = 0.65f

/** A placeholder for a card with a cover on the left and three lines of text next to it. */
@Composable
fun SkeletonRowView(modifier: Modifier = Modifier, coverWidth: Dp = DefaultCoverWidth) {
    val shape = MaterialTheme.shapes.medium
    Row(
        modifier = modifier
            .clip(shape)
            .cardBackground()
            .border(VndbTheme.dimens.borderWidth, VndbTheme.colors.borderSubtle, shape)
            .padding(VndbTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.md),
    ) {
        SkeletonBoxView(
            modifier = Modifier
                .width(coverWidth)
                .aspectRatio(VndbTheme.dimens.coverAspectRatio),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = VndbTheme.spacing.xs),
            verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
        ) {
            SkeletonLineView(widthFraction = TITLE_WIDTH, height = 18.dp)
            SkeletonLineView(widthFraction = SUBTITLE_WIDTH)
            SkeletonLineView(widthFraction = DETAIL_WIDTH)
        }
    }
}

@AdaptivePreviews
@Composable
private fun SkeletonRowViewPreview() {
    VndbTheme { SkeletonRowView(modifier = Modifier.fillMaxWidth()) }
}
