package com.zc.vndb.feature.detail.domain.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.detail.domain.model.CoverArt
import com.zc.vndb.feature.detail.domain.model.Edition
import com.zc.vndb.feature.detail.domain.model.NovelCharacter
import com.zc.vndb.feature.detail.domain.model.NovelCredits
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.domain.model.Quote
import com.zc.vndb.feature.detail.domain.model.ReleaseSummary
import com.zc.vndb.feature.detail.domain.model.ResultPage
import com.zc.vndb.feature.detail.domain.model.Screenshot

class FakeDetailRepository : DetailRepository {

    val novels = mutableMapOf<String, NovelDetail>()
    var characters: List<NovelCharacter> = emptyList()
    var credits = NovelCredits(emptyList(), emptyList(), emptyMap())
    var editions: List<Edition> = emptyList()
    var screenshots: List<Screenshot> = emptyList()
    var covers: List<CoverArt> = emptyList()
    var releases: List<ReleaseSummary> = emptyList()
    var quotes: List<Quote> = emptyList()
    var charactersError: DomainError? = null
    var creditsError: DomainError? = null
    var editionsError: DomainError? = null
    var screenshotsError: DomainError? = null
    var quotesError: DomainError? = null

    /** Every call, e.g. "screenshots" or "quotes:2", to check what loaded and when. */
    val calls = mutableListOf<String>()

    override suspend fun getNovel(id: String): Result<NovelDetail> {
        calls += "novel"
        return novels[id]?.let { Result.success(it) } ?: Result.failure(DomainError.NotFound)
    }

    override suspend fun getCharacters(novelId: String): Result<List<NovelCharacter>> {
        calls += "characters"
        return result(charactersError, characters)
    }

    override suspend fun getCredits(novelId: String): Result<NovelCredits> {
        calls += "credits"
        return result(creditsError, credits)
    }

    override suspend fun getEditions(novelId: String): Result<List<Edition>> {
        calls += "editions"
        return result(editionsError, editions)
    }

    override suspend fun getScreenshots(novelId: String): Result<List<Screenshot>> {
        calls += "screenshots"
        return result(screenshotsError, screenshots)
    }

    override suspend fun getCovers(novelId: String): Result<List<CoverArt>> {
        calls += "covers"
        return Result.success(covers)
    }

    override suspend fun getReleases(
        novelId: String,
        page: Int,
        pageSize: Int,
    ): Result<ResultPage<ReleaseSummary>> {
        calls += "releases:$page"
        return Result.success(releases.page(page, pageSize))
    }

    override suspend fun getQuotes(
        novelId: String,
        page: Int,
        pageSize: Int,
    ): Result<ResultPage<Quote>> {
        calls += "quotes:$page"
        return result(quotesError, quotes.page(page, pageSize))
    }

    private fun <T> result(error: DomainError?, value: T): Result<T> =
        error?.let { Result.failure(it) } ?: Result.success(value)

    private fun <T> List<T>.page(page: Int, size: Int) = ResultPage(
        items = drop((page - 1) * size).take(size),
        hasMore = page * size < this.size,
    )
}
