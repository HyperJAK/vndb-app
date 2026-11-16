package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.AgeRange
import com.zc.vndb.feature.detail.domain.model.DetailSamples.date
import com.zc.vndb.feature.detail.domain.model.DetailSamples.edition
import com.zc.vndb.feature.detail.domain.repository.FakeDetailRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class GetNovelEditionsUseCaseTest {

    private val repository = FakeDetailRepository()
    private val useCase = GetNovelEditionsUseCase(repository)

    @Test
    fun `earliest official non-patch edition per platform`() = runTest {
        repository.editions = listOf(
            edition("r-win-late", platform = "win", released = date(2016)),
            edition("r-win-early", platform = "win", released = date(2010)),
            edition("r-swi", platform = "swi", released = date(2018)),
            edition("r-fan", platform = "ios", official = false),
            edition("r-patch", platform = "and", patch = true),
        )

        val editions = useCase("v1").getOrThrow().editions

        assertEquals(listOf("r-win-early", "r-swi"), editions.map { it.id })
    }

    @Test
    fun `at most six platforms`() = runTest {
        repository.editions = (1..10).map { edition("r$it", platform = "p$it") }

        assertEquals(6, useCase("v1").getOrThrow().editions.size)
    }

    @Test
    fun `age range spans official releases with a rating`() = runTest {
        repository.editions = listOf(
            edition("r1", minAge = 0),
            edition("r2", minAge = 15),
            edition("r3", minAge = null),
            edition("r4", minAge = 18, official = false),
        )

        assertEquals(AgeRange(min = 0, max = 15), useCase("v1").getOrThrow().ageRange)
    }

    @Test
    fun `no age ratings means no range`() = runTest {
        repository.editions = listOf(edition("r1", minAge = null))

        assertNull(useCase("v1").getOrThrow().ageRange)
    }
}
