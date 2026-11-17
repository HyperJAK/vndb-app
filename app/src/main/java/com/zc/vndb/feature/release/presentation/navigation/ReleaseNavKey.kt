package com.zc.vndb.feature.release.presentation.navigation

import com.zc.vndb.common.ui.navigation.FullScreenNavKey
import kotlinx.serialization.Serializable

/** A release's page. */
@Serializable
data class ReleaseNavKey(val id: String) : FullScreenNavKey
