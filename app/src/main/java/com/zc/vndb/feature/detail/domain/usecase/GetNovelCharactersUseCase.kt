package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.feature.detail.domain.model.NovelCharacter
import com.zc.vndb.feature.detail.domain.repository.DetailRepository

/**
 * The VN's main and primary characters, main first, each with their voice actor from
 * [voiceActors] (character id → name, from the VN record).
 */
class GetNovelCharactersUseCase(private val repository: DetailRepository) {

    suspend operator fun invoke(
        novelId: String,
        voiceActors: Map<String, String>,
    ): Result<List<NovelCharacter>> = repository.getCharacters(novelId).map { characters ->
        characters
            .filter { it.role == CharacterRole.Main || it.role == CharacterRole.Primary }
            .sortedBy { it.role.ordinal }
            .take(MAX_CHARACTERS)
            .map { it.copy(voiceActor = voiceActors[it.id]) }
    }

    private companion object {
        const val MAX_CHARACTERS = 12
    }
}
