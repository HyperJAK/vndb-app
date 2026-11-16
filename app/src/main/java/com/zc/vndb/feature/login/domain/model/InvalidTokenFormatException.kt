package com.zc.vndb.feature.login.domain.model

/** The pasted text is not shaped like a VNDB token, so it is rejected before any request. */
class InvalidTokenFormatException : Exception("Not a VNDB token")
