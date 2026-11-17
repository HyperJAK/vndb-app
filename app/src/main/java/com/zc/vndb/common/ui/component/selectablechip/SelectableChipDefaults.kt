package com.zc.vndb.common.ui.component.selectablechip

import androidx.compose.runtime.Composable
import com.zc.vndb.common.ui.theme.TintedColors
import com.zc.vndb.common.ui.theme.VndbTheme

/** Default colors of [SelectableChipView], for callers that only override them sometimes. */
object SelectableChipDefaults {

    /** The accent fill of a selected chip. */
    @Composable
    fun colors(): TintedColors = TintedColors(
        container = VndbTheme.colors.chipActiveContainer,
        content = VndbTheme.colors.chipActiveContent,
        border = VndbTheme.colors.chipActiveContent,
    )
}
