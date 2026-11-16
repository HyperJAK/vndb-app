package com.zc.vndb.feature.discover.data.mapper

import com.zc.vndb.feature.discover.data.dto.StaffDTO
import com.zc.vndb.feature.discover.domain.model.StaffCredit
import com.zc.vndb.feature.discover.domain.model.StaffRole

fun StaffDTO.toDomain(): StaffCredit = StaffCredit(name = name, role = role.toStaffRole())

private fun String?.toStaffRole(): StaffRole = when (this) {
    "scenario" -> StaffRole.Scenario
    "chardesign" -> StaffRole.CharacterDesign
    "art" -> StaffRole.Art
    "music" -> StaffRole.Music
    "director" -> StaffRole.Director
    else -> StaffRole.Other
}
