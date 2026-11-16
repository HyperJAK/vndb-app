package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.feature.detail.domain.model.DetailSamples.date
import com.zc.vndb.feature.detail.domain.model.DetailSamples.novel
import com.zc.vndb.feature.detail.domain.model.DetailSamples.relation
import com.zc.vndb.feature.detail.domain.model.ExternalLink
import com.zc.vndb.feature.detail.domain.repository.FakeDetailRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class GetNovelDetailUseCaseTest {

    private val repository = FakeDetailRepository()
    private val useCase = GetNovelDetailUseCase(repository)

    @Test
    fun `orders relations official first, then oldest first, up to ten`() = runTest {
        repository.novels["v1"] = novel(
            id = "v1",
            relations = listOf(
                relation("v-fan", official = false, released = date(2010)),
                relation("v-new", released = date(2020)),
                relation("v-old", released = date(2012)),
                relation("v-tba", released = ReleaseDate.Tba),
            ) + (1..10).map { relation("v-x$it", official = false, released = date(2030)) },
        )

        val ids = useCase("v1").getOrThrow().relations.map { it.id }

        assertEquals(listOf("v-old", "v-new", "v-tba", "v-fan"), ids.take(4))
        assertEquals(10, ids.size)
    }

    @Test
    fun `keeps one link per site, up to three`() = runTest {
        repository.novels["v1"] = novel(
            id = "v1",
            links = listOf(
                ExternalLink("Wikipedia (en)", "a"),
                ExternalLink("GameFAQs", "b"),
                ExternalLink("GameFAQs", "c"),
                ExternalLink("Wikidata", "d"),
                ExternalLink("VGMdb", "e"),
            ),
        )

        assertEquals(listOf("a", "b", "d"), useCase("v1").getOrThrow().links.map { it.url })
    }

    @Test
    fun `propagates NotFound`() = runTest {
        assertEquals(DomainError.NotFound, useCase("missing").exceptionOrNull())
    }
}
