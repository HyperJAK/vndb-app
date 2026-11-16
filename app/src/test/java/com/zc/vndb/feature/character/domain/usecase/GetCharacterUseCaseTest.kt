package com.zc.vndb.feature.character.domain.usecase

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.character.domain.model.CharacterSamples.appearance
import com.zc.vndb.feature.character.domain.model.CharacterSamples.profile
import com.zc.vndb.feature.character.domain.repository.FakeCharacterRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class GetCharacterUseCaseTest {

    private val repository = FakeCharacterRepository()
    private val useCase = GetCharacterUseCase(repository)

    @Test
    fun `orders appearances by role, then oldest first, with voice actors`() = runTest {
        repository.characters["c1"] = profile(
            appearances = listOf(
                appearance("v-appears", CharacterRole.Appears, year = 2001),
                appearance("v-primary-new", CharacterRole.Primary, year = 2015),
                appearance("v-main", CharacterRole.Main, year = 2020),
                appearance("v-primary-old", CharacterRole.Primary, year = 2009),
            ),
        )
        repository.voiceActors = mapOf("v-primary-old" to "Imai Asami")

        val appearances = useCase("c1").getOrThrow().appearances

        assertEquals(
            listOf("v-main", "v-primary-old", "v-primary-new", "v-appears"),
            appearances.map { it.novelId },
        )
        assertEquals("Imai Asami", appearances[1].voiceActor)
        assertNull(appearances[0].voiceActor)
    }

    @Test
    fun `a failed voice-actor lookup still shows the character`() = runTest {
        repository.characters["c1"] = profile(appearances = listOf(appearance("v1")))
        repository.voiceActorsError = DomainError.Server

        assertNull(useCase("c1").getOrThrow().appearances.single().voiceActor)
    }

    @Test
    fun `propagates NotFound`() = runTest {
        assertEquals(DomainError.NotFound, useCase("missing").exceptionOrNull())
    }
}
