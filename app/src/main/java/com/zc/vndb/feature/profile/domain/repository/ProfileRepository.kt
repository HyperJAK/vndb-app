package com.zc.vndb.feature.profile.domain.repository

import com.zc.vndb.feature.profile.domain.model.EntryPage
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.domain.model.UserProfile

/**
 * The signed-in user's data. Requests carry the stored token, so private labels show; a rejected
 * token (`DomainError.Unauthorized`) also signs the user out.
 */
interface ProfileRepository {

    suspend fun getUser(userId: String): Result<UserProfile>

    suspend fun getLabels(userId: String): Result<List<ListLabel>>

    /** Label ids [userId] gave [novelId]; empty when it is not on their list. */
    suspend fun getEntryLabelIds(userId: String, novelId: String): Result<List<Int>>

    /** One page (1-based) of the entries with [labelId], most recently changed first. */
    suspend fun getEntries(
        userId: String,
        labelId: Int,
        page: Int,
        pageSize: Int,
    ): Result<EntryPage>
}
