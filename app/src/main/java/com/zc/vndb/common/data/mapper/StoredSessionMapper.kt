package com.zc.vndb.common.data.mapper

import com.zc.vndb.common.data.local.StoredSession
import com.zc.vndb.common.domain.model.Session

private const val LIST_READ = "listread"

fun StoredSession.toDomain() = Session(
    userId = userId,
    username = username,
    canReadPrivateList = LIST_READ in permissions,
)
