package com.zc.vndb.feature.detail.data.remote

import com.zc.vndb.common.data.dto.QueryDTO
import com.zc.vndb.feature.detail.data.dto.CharacterPageDTO
import com.zc.vndb.feature.detail.data.dto.CoverReleasePageDTO
import com.zc.vndb.feature.detail.data.dto.DetailNovelPageDTO
import com.zc.vndb.feature.detail.data.dto.EditionPageDTO
import com.zc.vndb.feature.detail.data.dto.NovelCreditsPageDTO
import com.zc.vndb.feature.detail.data.dto.NovelScreenshotsPageDTO
import com.zc.vndb.feature.detail.data.dto.QuotePageDTO
import com.zc.vndb.feature.detail.data.dto.ReleaseSummaryPageDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** One method per section of the page; the queries come from `DetailQueryDomainMapper`. */
class DetailService(private val client: HttpClient) {

    suspend fun queryNovel(query: QueryDTO): DetailNovelPageDTO = post("vn", query)

    suspend fun queryCredits(query: QueryDTO): NovelCreditsPageDTO = post("vn", query)

    suspend fun queryScreenshots(query: QueryDTO): NovelScreenshotsPageDTO = post("vn", query)

    suspend fun queryCharacters(query: QueryDTO): CharacterPageDTO = post("character", query)

    suspend fun queryEditions(query: QueryDTO): EditionPageDTO = post("release", query)

    suspend fun queryReleases(query: QueryDTO): ReleaseSummaryPageDTO = post("release", query)

    suspend fun queryCovers(query: QueryDTO): CoverReleasePageDTO = post("release", query)

    suspend fun queryQuotes(query: QueryDTO): QuotePageDTO = post("quote", query)

    private suspend inline fun <reified T> post(endpoint: String, query: QueryDTO): T =
        client.post(endpoint) { setBody(query) }.body()
}
