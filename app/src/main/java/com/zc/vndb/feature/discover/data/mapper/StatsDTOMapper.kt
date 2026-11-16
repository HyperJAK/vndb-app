package com.zc.vndb.feature.discover.data.mapper

import com.zc.vndb.feature.discover.data.dto.StatsDTO
import com.zc.vndb.feature.discover.domain.model.DatabaseStats

fun StatsDTO.toDomain(): DatabaseStats = DatabaseStats(novelCount = vn)
