package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.NovelCast
import com.zc.vndb.feature.detail.domain.repository.DetailRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Factory

/**
 * Characters (all roles, main first, with voice actors) and staff. Credits load alongside and are
 * optional: if they fail, characters still show, without voice actors or staff.
 */
@Factory
class GetNovelCastUseCase(private val repository: DetailRepository) {

    suspend operator fun invoke(novelId: String): Result<NovelCast> = coroutineScope {
        val credits = async { repository.getCredits(novelId).getOrNull() }
        repository.getCharacters(novelId).map { characters ->
            val loadedCredits = credits.await()
            val voiceActors = loadedCredits?.voiceActors.orEmpty()
            NovelCast(
                characters = characters
                    .sortedBy { it.role.ordinal }
                    .map { it.copy(voiceActor = voiceActors[it.id]) },
                staff = loadedCredits?.staff.orEmpty(),
                editions = loadedCredits?.editions.orEmpty(),
            )
        }.onFailure { credits.cancel() }
    }
}
