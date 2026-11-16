package com.zc.vndb.feature.profile.domain.model

/** The entry breaks one of VNDB's rules, so it was not sent. */
class InvalidListEntryException(val problems: Set<ListEntryProblem>) :
    IllegalArgumentException("Invalid list entry: $problems")
