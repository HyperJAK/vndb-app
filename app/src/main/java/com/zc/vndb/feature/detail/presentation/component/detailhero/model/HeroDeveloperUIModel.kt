package com.zc.vndb.feature.detail.presentation.component.detailhero.model

import androidx.compose.runtime.Immutable

/** One developer in the hero's "Developer:" line; opens its producer page when [id] is known. */
@Immutable
data class HeroDeveloperUIModel(val id: String?, val name: String)
