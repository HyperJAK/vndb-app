package com.zc.vndb.common.ui.util

import androidx.compose.runtime.staticCompositionLocalOf

/** Settings → "Show sensitive content": when true, sensitive covers are shown without blur. */
val LocalShowSensitiveContent = staticCompositionLocalOf { false }
