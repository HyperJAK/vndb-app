package com.zc.vndb.feature.character.presentation.navigation

import com.zc.vndb.common.ui.navigation.FullScreenNavKey
import kotlinx.serialization.Serializable

/** A character's page. */
@Serializable
data class CharacterNavKey(val id: String) : FullScreenNavKey
