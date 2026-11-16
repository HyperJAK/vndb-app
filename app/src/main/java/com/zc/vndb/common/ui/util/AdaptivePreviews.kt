package com.zc.vndb.common.ui.util

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

/** Light + dark at compact and expanded widths. Required on every Screen and View. */
@Preview(name = "Compact · Light", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(
    name = "Compact · Dark",
    widthDp = 360,
    heightDp = 800,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
)
@Preview(name = "Expanded · Light", widthDp = 1280, heightDp = 800, showBackground = true)
@Preview(
    name = "Expanded · Dark",
    widthDp = 1280,
    heightDp = 800,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
)
annotation class AdaptivePreviews
