package com.zc.vndb.feature.character.domain.usecase

import com.zc.vndb.feature.character.domain.model.CharacterAppearance
import com.zc.vndb.feature.character.domain.model.CharacterProfile
import com.zc.vndb.feature.character.domain.repository.CharacterRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * The character page. Voice actors load alongside and are optional: if that call fails the page
 * shows without them. Appearances are ordered by role (main first), then oldest first.
 */
class GetCharacterUseCase(private val repository: CharacterRepository) {

    suspend operator fun invoke(id: String): Result<CharacterProfile> = coroutineScope {
        val voiceActors = async { repository.getVoiceActors(id).getOrNull().orEmpty() }
        repository.getCharacter(id).map { profile ->
            val actors = voiceActors.await()
            profile.copy(
                appearances = profile.appearances
                    .sortedWith(
                        compareBy<CharacterAppearance>({ it.role.ordinal }, { it.releaseDate }),
                    )
                    .map { it.copy(voiceActor = actors[it.novelId]) },
            )
        }.onFailure { voiceActors.cancel() }
    }
}
