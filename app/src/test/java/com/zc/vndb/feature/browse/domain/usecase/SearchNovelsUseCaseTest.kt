package com.zc.vndb.feature.browse.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.model.TagCategory
import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowseSamples.novel
import com.zc.vndb.feature.browse.domain.model.BrowseSamples.tag
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import com.zc.vndb.feature.browse.domain.repository.FakeBrowseRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class SearchNovelsUseCaseTest {

    private val repository = FakeBrowseRepository()
    private val useCase = SearchNovelsUseCase(repository)

    @Test
    fun `trims the query and keeps relevance while searching`() = runTest {
        useCase(BrowseFilter(query = "  steins ", sort = BrowseSort.Relevance), page = 1)

        assertEquals(
            BrowseFilter(query = "steins", sort = BrowseSort.Relevance),
            repository.requests.single().filter,
        )
    }

    @Test
    fun `relevance without a query falls back to popularity`() = runTest {
        useCase(BrowseFilter(query = "   ", sort = BrowseSort.Relevance), page = 1)

        assertEquals(BrowseSort.Popularity, repository.requests.single().filter.sort)
    }

    @Test
    fun `asks for the total on the first page only, with the page size`() = runTest {
        useCase(BrowseFilter(), page = 1)
        useCase(BrowseFilter(), page = 2)

        assertTrue(repository.requests[0].withCount)
        assertFalse(repository.requests[1].withCount)
        assertEquals(listOf(1, 2), repository.requests.map { it.page })
        assertTrue(repository.requests.all { it.pageSize == SearchNovelsUseCase.PAGE_SIZE })
    }

    @Test
    fun `keeps the three most relevant spoiler-free content tags`() = runTest {
        repository.novels = listOf(
            novel(
                "v1",
                tags = listOf(
                    tag("Low", rating = 1.0),
                    tag("Spoiler", rating = 3.0, spoiler = 1),
                    tag("Tech", rating = 3.0, category = TagCategory.Technical),
                    tag("Sexual", rating = 3.0, category = TagCategory.Sexual),
                    tag("Top", rating = 2.9),
                    tag("Second", rating = 2.5),
                    tag("Third", rating = 2.0),
                ),
            ),
        )

        val tags = useCase(BrowseFilter(), page = 1).getOrThrow().novels.single().tags

        assertEquals(listOf("Top", "Second", "Third"), tags.map { it.name })
    }

    @Test
    fun `propagates repository errors`() = runTest {
        repository.error = DomainError.RateLimited

        assertEquals(DomainError.RateLimited, useCase(BrowseFilter(), page = 1).exceptionOrNull())
    }
}
