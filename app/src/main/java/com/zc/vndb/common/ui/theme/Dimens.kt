package com.zc.vndb.common.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Spacing scale from DESIGN.md. Read through [VndbTheme.spacing]. */
@Immutable
data class VndbSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    /** Grid gap on compact widths. */
    val gutter: Dp = 12.dp,
    /** Grid gap on expanded widths. */
    val gutterDesktop: Dp = 20.dp,
    /** Outer screen margin on compact widths. */
    val margin: Dp = 16.dp,
    /** Outer screen margin on expanded widths. */
    val marginDesktop: Dp = 32.dp,
)

/** Portrait cover cards use a 3:4 ratio (width / height). */
private const val COVER_ASPECT_RATIO = 0.75f

/** Fixed sizes and elevation effects from DESIGN.md. Read through [VndbTheme.dimens]. */
@Immutable
data class VndbDimens(
    val borderWidth: Dp = 1.dp,
    val focusRingWidth: Dp = 2.dp,
    val filterChipHeight: Dp = 28.dp,
    /** Level 2 glow: 0 4px 16px. */
    val glowOffsetY: Dp = 4.dp,
    val glowBlur: Dp = 16.dp,
    /** Level 3 glass surface backdrop blur. */
    val glassBlur: Dp = 16.dp,
    /** Major-spoiler text mask. */
    val spoilerBlur: Dp = 4.dp,
    /** Cover card width / height. */
    val coverAspectRatio: Float = COVER_ASPECT_RATIO,
)

internal val LocalVndbSpacing = staticCompositionLocalOf { VndbSpacing() }
internal val LocalVndbDimens = staticCompositionLocalOf { VndbDimens() }
