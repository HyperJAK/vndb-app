package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.model.VoiceActor
import com.zc.vndb.feature.detail.domain.model.DetailSamples.character
import com.zc.vndb.feature.detail.domain.model.DetailSamples.credits
import com.zc.vndb.feature.detail.domain.model.DetailSamples.staff
import com.zc.vndb.feature.detail.domain.model.StaffCreditRole
import com.zc.vndb.feature.detail.domain.repository.FakeDetailRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class GetNovelCastUseCaseTest {

    private val repository = FakeDetailRepository()
    private val useCase = GetNovelCastUseCase(repository)

    @Test
    fun `every character, main first, with voice actors and staff`() = runTest {
        repository.characters = listOf(
            character("c1", CharacterRole.Side),
            character("c2", CharacterRole.Main),
            character("c3", CharacterRole.Appears),
            character("c4", CharacterRole.Primary),
        )
        repository.credits = credits(
            staff = listOf(staff("s1", StaffCreditRole.Scenario)),
            voiceActors = mapOf("c2" to VoiceActor("s7", "Miyano Mamoru")),
        )

        val cast = useCase("v1").getOrThrow()

        assertEquals(listOf("c2", "c4", "c1", "c3"), cast.characters.map { it.id })
        assertEquals(VoiceActor("s7", "Miyano Mamoru"), cast.characters.first().voiceActor)
        assertNull(cast.characters[1].voiceActor)
        assertEquals(listOf("s1"), cast.staff.map { it.staffId })
    }

    @Test
    fun `failed credits still show characters`() = runTest {
        repository.characters = listOf(character("c1"))
        repository.creditsError = DomainError.Server

        val cast = useCase("v1").getOrThrow()

        assertEquals(listOf("c1"), cast.characters.map { it.id })
        assertTrue(cast.staff.isEmpty())
    }

    @Test
    fun `failed characters fail the section`() = runTest {
        repository.charactersError = DomainError.Timeout

        assertEquals(DomainError.Timeout, useCase("v1").exceptionOrNull())
    }
}
