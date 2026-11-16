package com.zc.vndb.feature.character.presentation.component.characterhero.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText

/** A "Label: value" line, e.g. "Height: 160 cm". */
@Immutable
data class CharacterFactUIModel(val label: StringResource, val value: UiText)
