package com.zc.vndb.feature.profile.domain.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.profile.domain.model.EntryPage
import com.zc.vndb.feature.profile.domain.model.ListEntry
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.domain.model.UserProfile

class FakeProfileRepository : ProfileRepository {

    var user: UserProfile? = null
    var labels: List<ListLabel> = emptyList()
    val entries = mutableMapOf<Int, List<ListEntry>>()
    var labelsError: DomainError? = null
    var entriesError: DomainError? = null

    /** "labels", "user", "entries:<label>:<page>". */
    val calls = mutableListOf<String>()

    override suspend fun getUser(userId: String): Result<UserProfile> {
        calls += "user"
        return user?.let { Result.success(it) } ?: Result.failure(DomainError.NotFound)
    }

    override suspend fun getLabels(userId: String): Result<List<ListLabel>> {
        calls += "labels"
        return labelsError?.let { Result.failure(it) } ?: Result.success(labels)
    }

    override suspend fun getEntries(
        userId: String,
        labelId: Int,
        page: Int,
        pageSize: Int,
    ): Result<EntryPage> {
        calls += "entries:$labelId:$page"
        entriesError?.let { return Result.failure(it) }
        val all = entries[labelId].orEmpty()
        return Result.success(
            EntryPage(
                entries = all.drop((page - 1) * pageSize).take(pageSize),
                hasMore = page * pageSize < all.size,
            ),
        )
    }
}
