package com.zc.vndb.feature.detail.presentation.component.staffrole.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList

/** One line of the staff section: a role and the people credited for it. */
@Immutable
data class StaffRoleUIModel(val label: UiText, val people: ImmutableList<StaffPersonUIModel>)

/** A credited person. [note] is the credit's own remark, such as a route or a song. */
@Immutable
data class StaffPersonUIModel(val id: String, val name: String, val note: String?)
