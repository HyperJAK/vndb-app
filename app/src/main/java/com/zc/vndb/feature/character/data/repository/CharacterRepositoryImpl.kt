package com.zc.vndb.feature.character.data.repository

import com.zc.vndb.common.data.util.safeApiCall
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.model.VoiceActor
import com.zc.vndb.feature.character.data.mapper.characterQuery
import com.zc.vndb.feature.character.data.mapper.characterVoicesQuery
import com.zc.vndb.feature.character.data.mapper.toDomain
import com.zc.vndb.feature.character.data.mapper.toVoiceActors
import com.zc.vndb.feature.character.data.remote.CharacterService
import com.zc.vndb.feature.character.domain.model.CharacterProfile
import com.zc.vndb.feature.character.domain.repository.CharacterRepository
import org.koin.core.annotation.Single

@Single
class CharacterRepositoryImpl(private val service: CharacterService) : CharacterRepository {

    override suspend fun getCharacter(id: String): Result<CharacterProfile> = safeApiCall {
        val character = service.queryCharacter(
            characterQuery(id),
        ).results.firstOrNull() ?: throw DomainError.NotFound
        character.toDomain()
    }

    override suspend fun getVoiceActors(characterId: String): Result<Map<String, VoiceActor>> =
        safeApiCall {
            service.queryNovelVoices(characterVoicesQuery(characterId)).toVoiceActors(characterId)
        }
}
