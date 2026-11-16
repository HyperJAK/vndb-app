package com.zc.vndb.feature.discover.data.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.feature.discover.data.remote.DiscoverApiStub
import com.zc.vndb.feature.discover.data.remote.DiscoverApiStub.Companion.fixture
import com.zc.vndb.feature.discover.domain.model.Platform
import com.zc.vndb.feature.discover.domain.model.StaffRole
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate

class DiscoverRepositoryImplTest {

    private val api = DiscoverApiStub()
    private val repository = DiscoverRepositoryImpl(api.service)

    @Test
    fun `maps the top-rated fixture to domain novels`() = runTest {
        api.body = fixture("top_rated.json")

        val first = repository.getTopRatedNovels(limit = 3).getOrThrow().first()

        assertEquals("v20802", first.id)
        assertEquals("Rance X -Kessen-", first.title)
        assertEquals(90.7, first.rating)
        assertEquals("Alice Soft", first.developerName)
        assertEquals("https://t.vndb.org/cv/91/88391.jpg", first.cover?.url)
    }

    @Test
    fun `maps the details fixture including staff and tags`() = runTest {
        api.body = fixture("details.json")

        val details = repository.getNovelDetails("v2002").getOrThrow()

        assertEquals(listOf("NITRO PLUS", "MAGES.", "MAGES. Inc."), details.developerNames)
        assertEquals("Tanizaki Ouka", details.staff.single { it.role == StaffRole.Scenario }.name)
        assertEquals(5, details.tags.size)
    }

    @Test
    fun `details for an unknown id is NotFound`() = runTest {
        api.body = """{"more":false,"results":[]}"""

        assertEquals(DomainError.NotFound, repository.getNovelDetails("v0").exceptionOrNull())
    }

    @Test
    fun `maps the releases fixture`() = runTest {
        api.body = fixture("releases.json")

        val release = repository.getRecentReleases(
            listOf("en", "ja"),
            limit = 3,
        ).getOrThrow().first()

        assertEquals(ReleaseDate.Full(LocalDate(2026, 11, 16)), release.releaseDate)
        assertEquals(listOf(Platform("win")), release.platforms)
        assertEquals("v63472", release.novels.single().id)
    }

    @Test
    fun `maps stats`() = runTest {
        api.body = fixture("stats.json")

        assertEquals(68053, repository.getDatabaseStats().getOrThrow().novelCount)
    }

    @Test
    fun `latest novel number comes from the highest id, or 0 when empty`() = runTest {
        api.body = """{"more":true,"results":[{"id":"v70743"}]}"""
        assertEquals(70743, repository.getLatestNovelNumber().getOrThrow())

        api.body = """{"more":false,"results":[]}"""
        assertEquals(0, repository.getLatestNovelNumber().getOrThrow())
    }

    @Test
    fun `first novel from a number is its id, or null when none`() = runTest {
        api.body = """{"more":true,"results":[{"id":"v30001"}]}"""
        assertEquals("v30001", repository.getFirstNovelIdFrom(30000).getOrThrow())

        api.body = """{"more":false,"results":[]}"""
        assertNull(repository.getFirstNovelIdFrom(99999).getOrThrow())
    }

    @Test
    fun `HTTP failures become domain errors`() = runTest {
        api.status = HttpStatusCode.TooManyRequests
        assertEquals(
            DomainError.RateLimited,
            repository.getTopRatedNovels(limit = 3).exceptionOrNull(),
        )

        api.status = HttpStatusCode.BadGateway
        assertEquals(DomainError.Server, repository.getDatabaseStats().exceptionOrNull())
    }

    @Test
    fun `an unexpected response shape is InvalidData`() = runTest {
        api.body = """{"results":"nope"}"""

        assertEquals(
            DomainError.InvalidData,
            repository.getTopRatedNovels(limit = 3).exceptionOrNull(),
        )
    }
}
