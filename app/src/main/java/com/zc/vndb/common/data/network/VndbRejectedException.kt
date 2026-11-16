package com.zc.vndb.common.data.network

/** VNDB answered 400 Bad Request; [reason] is the text it sent back. */
class VndbRejectedException(val reason: String) : Exception(reason)
