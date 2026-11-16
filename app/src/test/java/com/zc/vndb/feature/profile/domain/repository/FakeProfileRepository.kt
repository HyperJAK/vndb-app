package com.zc.vndb.feature.profile.domain.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.profile.domain.model.EntryOrder
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

    /** Label ids per novel on the user's list. */
    val entryLabels = mutableMapOf<String, List<Int>>()

    /** "labels", "user", "entries:<label>:<page>". */
    val calls = mutableListOf<String>()

    data class EntriesRequest(
        val labelId: Int,
        val page: Int,
        val order: EntryOrder,
        val ascending: Boolean,
        val voted: Boolean?,
    )

    val requests = mutableListOf<EntriesRequest>()

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
        order: EntryOrder,
        ascending: Boolean,
        voted: Boolean?,
    ): Result<EntryPage> {
        calls += "entries:$labelId:$page"
        requests += EntriesRequest(labelId, page, order, ascending, voted)
        entriesError?.let { return Result.failure(it) }
        val all = entries[labelId].orEmpty()
            .filter { voted == null || (it.vote != null) == voted }
            .let { list ->
                val ascendingList = when (order) {
                    EntryOrder.Title -> list.sortedBy { it.title }
                    EntryOrder.Updated -> list.sortedBy { it.lastModified }
                    EntryOrder.Vote -> list.sortedBy { it.vote ?: -1 }
                }
                if (ascending) ascendingList else ascendingList.reversed()
            }
        return Result.success(
            EntryPage(
                entries = all.drop((page - 1) * pageSize).take(pageSize),
                hasMore = page * pageSize < all.size,
            ),
        )
    }

    override suspend fun getEntryLabelIds(userId: String, novelId: String): Result<List<Int>> {
        calls += "entryLabels:$novelId"
        return Result.success(entryLabels[novelId].orEmpty())
    }
}
