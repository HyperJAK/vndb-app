package com.zc.vndb.common.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

// "Soft" roundedness profile from DESIGN.md:
// sm 2dp · DEFAULT 4dp · md 6dp · lg 8dp · xl 12dp · full = pill
val VndbShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp), // controls, inputs, chips
    medium = RoundedCornerShape(6.dp), // cards, cover art
    large = RoundedCornerShape(8.dp),
    extraLarge = RoundedCornerShape(12.dp), // sheets, dialogs
)

/** Shapes from DESIGN.md that have no Material 3 slot. Read through [VndbTheme.shapes]. */
@Immutable
data class VndbExtraShapes(
    /** Badges, spoiler tags, score chips, status pills. */
    val pill: Shape = CircleShape,
    /** Portrait cover cards (3:4). */
    val cover: Shape = RoundedCornerShape(6.dp),
)

internal val LocalVndbShapes = staticCompositionLocalOf { VndbExtraShapes() }
