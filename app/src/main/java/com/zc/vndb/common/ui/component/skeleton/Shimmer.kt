package com.zc.vndb.common.ui.component.skeleton

import android.provider.Settings
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme

private const val SHIMMER_DURATION_MILLIS = 1200
private const val HIGHLIGHT_MIX = 0.12f

/**
 * Fills the node with a placeholder color and a highlight that sweeps across it. The sweep is read
 * in the draw phase only, so it never recomposes. It stays still when the user turned animations
 * off.
 */
@Composable
fun Modifier.shimmer(): Modifier {
    val base = VndbTheme.colors.surfaceInset
    val highlight = lerp(base, MaterialTheme.colorScheme.onSurface, HIGHLIGHT_MIX)
    val context = LocalContext.current
    val animationsEnabled = remember(context) {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f,
        ) != 0f
    }
    if (!animationsEnabled) return background(base)
    val progress = rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(SHIMMER_DURATION_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerProgress",
    )
    return drawBehind {
        val startX = (progress.value * 2f - 1f) * size.width
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(base, highlight, base),
                start = Offset(startX, 0f),
                end = Offset(startX + size.width, 0f),
            ),
        )
    }
}

/** Announces a whole skeleton as one "Loading" node; its placeholder shapes stay silent. */
@Composable
fun Modifier.loadingSemantics(): Modifier {
    val description = UiText.StringResource(StringResource.cd_loading).asString()
    return semantics { contentDescription = description }
}
