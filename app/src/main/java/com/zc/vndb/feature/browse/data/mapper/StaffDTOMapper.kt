package com.zc.vndb.feature.browse.data.mapper

import com.zc.vndb.common.domain.model.ExternalLink
import com.zc.vndb.feature.browse.data.dto.StaffDTO
import com.zc.vndb.feature.browse.domain.model.StaffGender
import com.zc.vndb.feature.browse.domain.model.StaffInfo

fun StaffDTO.toDomain(): StaffInfo {
    val originalName = original?.takeIf { it.isNotBlank() && it != name }
    return StaffInfo(
        id = id,
        name = name,
        originalName = originalName,
        // Each alias shows romanised when VNDB has that; the main names are already on the page.
        aliases = aliases.orEmpty()
            .map { (it.latin ?: it.name).trim() }
            .filter { it.isNotEmpty() && it != name && it != originalName }
            .distinct(),
        language = lang?.takeIf { it.isNotBlank() },
        gender = when (gender) {
            "m" -> StaffGender.Male
            "f" -> StaffGender.Female
            else -> null
        },
        description = description?.takeIf { it.isNotBlank() },
        links = extlinks.orEmpty().map { ExternalLink(label = it.label, url = it.url) },
    )
}
