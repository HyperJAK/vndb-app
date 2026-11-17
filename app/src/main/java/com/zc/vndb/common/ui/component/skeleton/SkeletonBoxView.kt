package com.zc.vndb.common.ui.component.skeleton

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

/** A shimmering placeholder block. Give it a size through [modifier]. */
@Composable
fun SkeletonBoxView(modifier: Modifier = Modifier, shape: Shape = MaterialTheme.shapes.small) {
    Box(modifier = modifier.clip(shape).shimmer())
}

@AdaptivePreviews
@Composable
private fun SkeletonBoxViewPreview() {
    VndbTheme {
        SkeletonBoxView(modifier = Modifier.size(width = 160.dp, height = 24.dp))
    }
}
