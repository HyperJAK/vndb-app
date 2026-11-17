package com.zc.vndb.feature.detail.presentation.navigation

import com.zc.vndb.common.ui.navigation.FullScreenNavKey
import kotlinx.serialization.Serializable

/** A visual novel's detail page. */
@Serializable
data class DetailNavKey(val id: String) : FullScreenNavKey
