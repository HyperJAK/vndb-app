package com.zc.vndb.feature.character.domain.repository

import com.zc.vndb.feature.character.domain.model.CharacterProfile

interface CharacterRepository {

    /** The character, with one appearance per VN (voice actors not filled in). */
    suspend fun getCharacter(id: String): Result<CharacterProfile>

    /** VN id → the character's voice actor in that VN. */
    suspend fun getVoiceActors(characterId: String): Result<Map<String, String>>
}
