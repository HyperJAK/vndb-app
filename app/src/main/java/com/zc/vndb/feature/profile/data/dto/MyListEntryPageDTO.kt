package com.zc.vndb.feature.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyListEntryPageDTO(@SerialName("results") val results: List<MyListEntryDTO>)
