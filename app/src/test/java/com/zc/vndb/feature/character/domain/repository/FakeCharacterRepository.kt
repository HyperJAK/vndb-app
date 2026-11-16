package com.zc.vndb.feature.character.domain.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.character.domain.model.CharacterProfile

class FakeCharacterRepository : CharacterRepository {

    val characters = mutableMapOf<String, CharacterProfile>()
    var voiceActors: Map<String, String> = emptyMap()
    var voiceActorsError: DomainError? = null

    override suspend fun getCharacter(id: String): Result<CharacterProfile> =
        characters[id]?.let { Result.success(it) } ?: Result.failure(DomainError.NotFound)

    override suspend fun getVoiceActors(characterId: String): Result<Map<String, String>> =
        voiceActorsError?.let { Result.failure(it) } ?: Result.success(voiceActors)
}
