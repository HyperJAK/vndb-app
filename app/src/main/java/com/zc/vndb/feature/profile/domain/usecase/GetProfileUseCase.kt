package com.zc.vndb.feature.profile.domain.usecase

import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.domain.model.ProfileOverview
import com.zc.vndb.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * The user's labels (non-empty ones: predefined, then the user's own, then the virtual Voted and
 * No label) and profile, in parallel.
 * Labels are required; the profile line is optional.
 */
class GetProfileUseCase(private val repository: ProfileRepository) {

    suspend operator fun invoke(userId: String): Result<ProfileOverview> = coroutineScope {
        val user = async { repository.getUser(userId).getOrNull() }
        repository.getLabels(userId)
            .map { labels ->
                ProfileOverview(
                    user = user.await(),
                    labels = labels
                        .filter { it.count > 0 }
                        .sortedWith(compareBy({ it.id.virtualRank() }, { it.id })),
                )
            }
            .onFailure { user.cancel() }
    }
}

/** Real labels first (0), then Voted (1), then No label (2). */
private fun Int.virtualRank(): Int = when (this) {
    ListLabel.VOTED -> 1
    ListLabel.NO_LABEL -> 2
    else -> 0
}
