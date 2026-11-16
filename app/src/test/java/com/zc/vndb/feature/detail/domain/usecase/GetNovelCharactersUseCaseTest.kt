package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.detail.domain.model.CharacterRole
import com.zc.vndb.feature.detail.domain.model.DetailSamples.character
import com.zc.vndb.feature.detail.domain.repository.FakeDetailRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class GetNovelCharactersUseCaseTest {

    private val repository = FakeDetailRepository()
    private val useCase = GetNovelCharactersUseCase(repository)

    @Test
    fun `keeps main and primary characters, main first, with their voice actors`() = runTest {
        repository.characters = listOf(
            character("c1", CharacterRole.Primary),
            character("c2", CharacterRole.Side),
            character("c3", CharacterRole.Main),
            character("c4", CharacterRole.Appears),
            character("c5", CharacterRole.Primary),
        )

        val result = useCase("v1", voiceActors = mapOf("c3" to "Miyano Mamoru")).getOrThrow()

        assertEquals(listOf("c3", "c1", "c5"), result.map { it.id })
        assertEquals("Miyano Mamoru", result.first().voiceActor)
        assertNull(result[1].voiceActor)
    }

    @Test
    fun `shows at most twelve`() = runTest {
        repository.characters = (1..20).map { character("c$it") }

        assertEquals(12, useCase("v1", emptyMap()).getOrThrow().size)
    }

    @Test
    fun `propagates errors`() = runTest {
        repository.charactersError = DomainError.Server

        assertEquals(DomainError.Server, useCase("v1", emptyMap()).exceptionOrNull())
    }
}
