package com.zc.vndb.feature.discover.data.remote

import com.zc.vndb.common.data.dto.QueryDTO
import com.zc.vndb.feature.discover.data.dto.QuotePageDTO
import com.zc.vndb.feature.discover.data.dto.ReleasePageDTO
import com.zc.vndb.feature.discover.data.dto.StatsDTO
import com.zc.vndb.feature.discover.data.dto.VisualNovelPageDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Single

/** VNDB Kana endpoints used by Discover. Returns DTOs only; no mapping here. */
@Single
class DiscoverService(private val client: HttpClient) {

    suspend fun queryNovels(query: QueryDTO): VisualNovelPageDTO = client.post(
        "vn",
    ) { setBody(query) }.body()

    suspend fun queryReleases(query: QueryDTO): ReleasePageDTO = client.post(
        "release",
    ) { setBody(query) }.body()

    suspend fun queryQuotes(query: QueryDTO): QuotePageDTO = client.post(
        "quote",
    ) { setBody(query) }.body()

    suspend fun getStats(): StatsDTO = client.get("stats").body()
}
