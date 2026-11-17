package com.zc.vndb.common.ui.component.skeleton

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.theme.cardBackground
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val HeroCoverWidth = 112.dp
private val DefaultMaxWidth = 720.dp
private const val SECTION_COUNT = 3
private const val SECTION_LINES = 3
private const val HERO_TITLE_WIDTH = 0.9f
private const val HERO_SUBTITLE_WIDTH = 0.6f
private const val HERO_DETAIL_WIDTH = 0.4f
private const val LAST_LINE_WIDTH = 0.55f

/**
 * A placeholder for a detail page: a hero (cover and titles) above a few text sections. Used by
 * the novel, character and release pages while they load.
 */
@Composable
fun PageSkeletonView(modifier: Modifier = Modifier, maxWidth: Dp = DefaultMaxWidth) {
    Box(
        modifier = modifier.loadingSemantics(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .fillMaxSize()
                .padding(
                    horizontal = VndbTheme.spacing.margin,
                    vertical = VndbTheme.spacing.sm,
                ),
            verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.md),
        ) {
            Hero()
            repeat(SECTION_COUNT) { SectionCard() }
        }
    }
}

@Composable
private fun Hero() {
    Row(horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.lg)) {
        SkeletonBoxView(
            modifier = Modifier
                .width(HeroCoverWidth)
                .aspectRatio(VndbTheme.dimens.coverAspectRatio),
            shape = MaterialTheme.shapes.medium,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
        ) {
            SkeletonLineView(widthFraction = HERO_TITLE_WIDTH, height = 24.dp)
            SkeletonLineView(widthFraction = HERO_SUBTITLE_WIDTH)
            SkeletonLineView(widthFraction = HERO_DETAIL_WIDTH)
        }
    }
}

@Composable
private fun SectionCard() {
    val shape = MaterialTheme.shapes.medium
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .cardBackground()
            .border(VndbTheme.dimens.borderWidth, VndbTheme.colors.borderSubtle, shape)
            .padding(VndbTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
    ) {
        SkeletonBoxView(modifier = Modifier.width(120.dp).height(18.dp))
        repeat(SECTION_LINES - 1) { SkeletonLineView() }
        SkeletonLineView(widthFraction = LAST_LINE_WIDTH)
    }
}

@AdaptivePreviews
@Composable
private fun PageSkeletonViewPreview() {
    VndbTheme { PageSkeletonView(modifier = Modifier.fillMaxSize()) }
}
