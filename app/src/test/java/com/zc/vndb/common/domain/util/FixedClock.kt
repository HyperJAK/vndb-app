package com.zc.vndb.common.domain.util

import kotlin.time.Clock
import kotlin.time.Instant

/** A [Clock] stuck at [instant]; set [instant] to move time in a test. */
class FixedClock(var instant: Instant) : Clock {
    override fun now(): Instant = instant
}
