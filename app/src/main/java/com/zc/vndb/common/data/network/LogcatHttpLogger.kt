package com.zc.vndb.common.data.network

import android.util.Log
import io.ktor.client.plugins.logging.Logger

/** Logcat tag for VNDB API traffic: `adb logcat -s VndbHttp`. */
const val HTTP_LOG_TAG = "VndbHttp"

/** Logcat drops anything past ~4 KB per line, so long JSON bodies are split. */
private const val MAX_LOGCAT_CHUNK = 3_500

/** Sends Ktor's HTTP log to logcat under [HTTP_LOG_TAG]. Debug builds only (see CommonModule). */
class LogcatHttpLogger : Logger {
    override fun log(message: String) {
        message.chunked(MAX_LOGCAT_CHUNK).forEach { chunk -> Log.d(HTTP_LOG_TAG, chunk) }
    }
}
