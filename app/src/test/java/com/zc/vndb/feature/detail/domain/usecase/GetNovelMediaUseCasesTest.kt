package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.DetailSamples.coverArt
import com.zc.vndb.feature.detail.domain.model.DetailSamples.releaseSummary
import com.zc.vndb.feature.detail.domain.model.DetailSamples.screenshot
import com.zc.vndb.feature.detail.domain.repository.FakeDetailRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class GetNovelMediaUseCasesTest {

    private val repository = FakeDetailRepository()

    @Test
    fun `screenshots are grouped by release in first-seen order`() = runTest {
        repository.screenshots = listOf(
            screenshot("a", release = "r2"),
            screenshot("b", release = "r1"),
            screenshot("c", release = "r2"),
        )

        val result = GetNovelScreenshotsUseCase(repository)("v1").getOrThrow()

        assertEquals(listOf("a", "c", "b"), result.map { it.image.url })
    }

    @Test
    fun `covers are distinct, package fronts first`() = runTest {
        repository.covers = listOf(
            coverArt("dig", packageFront = false),
            coverArt("front", release = "r1"),
            coverArt("front", release = "r2"),
        )

        val result = GetNovelCoversUseCase(repository)("v1").getOrThrow()

        assertEquals(listOf("front", "dig"), result.map { it.image.url })
    }

    @Test
    fun `releases are paged by fifty`() = runTest {
        repository.releases = (1..60).map { releaseSummary("r$it") }
        val useCase = GetNovelReleasesUseCase(repository)

        val first = useCase("v1", page = 1).getOrThrow()
        val second = useCase("v1", page = 2).getOrThrow()

        assertEquals(50, first.items.size)
        assertTrue(first.hasMore)
        assertEquals(10, second.items.size)
        assertFalse(second.hasMore)
    }
}
