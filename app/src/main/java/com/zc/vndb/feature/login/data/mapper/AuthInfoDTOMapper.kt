package com.zc.vndb.feature.login.data.mapper

import com.zc.vndb.common.data.local.StoredSession
import com.zc.vndb.feature.login.data.dto.AuthInfoDTO

fun AuthInfoDTO.toStoredSession(token: String) = StoredSession(
    token = token,
    userId = id,
    username = username,
    permissions = permissions.orEmpty().toSet(),
)
