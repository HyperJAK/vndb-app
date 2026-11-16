package com.zc.vndb.feature.login.domain.model

import com.zc.vndb.common.domain.model.Session

object LoginSamples {

    /** The example token from the VNDB API docs (not a real account). */
    const val TOKEN = "hsoo-ybws4-j8yb9-qxkw-5obay-px8to-bfyk"

    fun session() = Session(userId = "u2", username = "Yorhel", canReadPrivateList = true)
}
