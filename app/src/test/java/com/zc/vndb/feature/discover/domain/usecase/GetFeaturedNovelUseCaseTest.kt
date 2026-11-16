package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.util.FixedClock
import com.zc.vndb.feature.discover.domain.model.Cover
import com.zc.vndb.feature.discover.domain.model.DiscoverSamples.details
import com.zc.vndb.feature.discover.domain.model.DiscoverSamples.novel
import com.zc.vndb.feature.discover.domain.model.DiscoverSamples.tag
import com.zc.vndb.feature.discover.domain.model.NovelDetails
import com.zc.vndb.feature.discover.domain.model.StaffCredit
import com.zc.vndb.feature.discover.domain.model.StaffRole
import com.zc.vndb.feature.discover.domain.model.TagCategory
import com.zc.vndb.feature.discover.domain.repository.FakeDiscoverRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant
import kotlinx.coroutines.test.runTest

class GetFeaturedNovelUseCaseTest {

    private val repository = FakeDiscoverRepository()
    private val clock = FixedClock(Instant.parse("2026-11-16T09:00:00Z")) // a Monday
    private val useCase = GetFeaturedNovelUseCase(repository, clock)

    private val pool = (1..50).map { novel("v$it") }

    init {
        pool.forEach { repository.details[it.id] = details(id = it.id) }
    }

    private suspend fun pickAt(instant: String): String {
        clock.instant = Instant.parse(instant)
        return useCase(pool).getOrThrow().id
    }

    @Test
    fun `the pick stays the same from Monday to Sunday`() = runTest {
        val monday = pickAt("2026-11-16T00:00:00Z")

        assertEquals(monday, pickAt("2026-11-19T12:00:00Z"))
        assertEquals(monday, pickAt("2026-11-22T23:59:59Z"))
    }

    @Test
    fun `the pick changes on the next Monday`() = runTest {
        assertNotEquals(pickAt("2026-11-22T23:59:59Z"), pickAt("2026-11-23T00:00:00Z"))
    }

    @Test
    fun `an empty pool is NotFound`() = runTest {
        assertEquals(DomainError.NotFound, useCase(emptyList()).exceptionOrNull())
    }

    @Test
    fun `propagates detail errors`() = runTest {
        repository.details.clear()

        assertEquals(DomainError.NotFound, useCase(pool).exceptionOrNull())
    }

    @Test
    fun `uses the first developer and the first scenario writer`() = runTest {
        stubAll(
            details(
                developers = listOf("TYPE-MOON", "Other"),
                staff = listOf(
                    StaffCredit("Koyama Hirokazu", StaffRole.Art),
                    StaffCredit("Nasu Kinoko", StaffRole.Scenario),
                    StaffCredit("Someone", StaffRole.Scenario),
                ),
            ),
        )

        val featured = useCase(pool).getOrThrow()

        assertEquals("TYPE-MOON", featured.developerName)
        assertEquals("Nasu Kinoko", featured.scenarioWriter)
    }

    @Test
    fun `missing developer and writer are null`() = runTest {
        stubAll(details(developers = emptyList(), staff = emptyList()))

        val featured = useCase(pool).getOrThrow()

        assertNull(featured.developerName)
        assertNull(featured.scenarioWriter)
    }

    @Test
    fun `keeps the two most relevant spoiler-free content tags`() = runTest {
        stubAll(
            details(
                tags = listOf(
                    tag("Kinetic Novel", rating = 2.5),
                    tag("Spoiler", rating = 3.0, spoiler = 2),
                    tag("Technical", rating = 3.0, category = TagCategory.Technical),
                    tag("Urban Fantasy", rating = 2.8),
                    tag("Mansion", rating = 1.0),
                ),
            ),
        )

        assertEquals(listOf("Urban Fantasy", "Kinetic Novel"), useCase(pool).getOrThrow().tags)
    }

    @Test
    fun `safe cover without sexual tags is all ages`() = runTest {
        stubAll(details(tags = listOf(tag("Urban Fantasy"))))

        assertTrue(useCase(pool).getOrThrow().isAllAges)
    }

    @Test
    fun `sexual tags, a suggestive cover or no cover are not all ages`() = runTest {
        val suggestive = Cover(url = "u", thumbnailUrl = null, sexual = 0.6, violence = 0.0)
        listOf(
            details(tags = listOf(tag("Sexual Content", category = TagCategory.Sexual))),
            details(cover = suggestive),
            details(cover = null),
        ).forEach { novelDetails ->
            stubAll(novelDetails)
            assertFalse(useCase(pool).getOrThrow().isAllAges)
        }
    }

    private fun stubAll(template: NovelDetails) {
        pool.forEach { repository.details[it.id] = template.copy(id = it.id) }
    }
}
