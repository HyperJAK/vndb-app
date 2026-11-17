package com.zc.vndb.common.ui.component.skeleton

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val DefaultLineHeight = 14.dp

/** A placeholder for one line of text, [widthFraction] of the available width. */
@Composable
fun SkeletonLineView(
    modifier: Modifier = Modifier,
    widthFraction: Float = 1f,
    height: Dp = DefaultLineHeight,
) {
    SkeletonBoxView(
        modifier = modifier
            .fillMaxWidth(widthFraction)
            .height(height),
    )
}

@AdaptivePreviews
@Composable
private fun SkeletonLineViewPreview() {
    VndbTheme { SkeletonLineView(widthFraction = 0.6f) }
}
