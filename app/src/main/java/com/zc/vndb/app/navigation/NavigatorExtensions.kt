package com.zc.vndb.app.navigation

import com.zc.vndb.common.ui.navigation.Navigator

/** Pops the current destination (reads as the other `navigateTo…` helpers in [appEntryProvider]). */
internal fun Navigator.navigateBack() = goBack()
