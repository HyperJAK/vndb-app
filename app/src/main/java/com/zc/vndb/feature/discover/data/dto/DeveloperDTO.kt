package com.zc.vndb.feature.discover.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeveloperDTO(@SerialName("name") val name: String)
