package com.zc.vndb.feature.profile.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.repository.SessionRepository
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.domain.model.MyListEntry
import com.zc.vndb.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

/** The signed-in user's labels that can be put on an entry (every label but the virtual ones). */
@Factory
class GetEditableLabelsUseCase(
    private val sessions: SessionRepository,
    private val repository: ProfileRepository,
) {

    suspend operator fun invoke(): Result<List<ListLabel>> {
        val session = sessions.session.first() ?: return Result.failure(DomainError.Unauthorized)
        return repository.getLabels(session.userId).map { labels ->
            labels.filterNot { it.id in MyListEntry.VIRTUAL_LABELS }.sortedBy { it.id }
        }
    }
}
