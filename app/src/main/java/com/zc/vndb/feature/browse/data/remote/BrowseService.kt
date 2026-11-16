package com.zc.vndb.feature.browse.data.remote

import com.zc.vndb.common.data.dto.QueryDTO
import com.zc.vndb.feature.browse.data.dto.BrowseNovelPageDTO
import com.zc.vndb.feature.browse.data.dto.TagInfoPageDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class BrowseService(private val client: HttpClient) {

    suspend fun queryTag(query: QueryDTO): TagInfoPageDTO = client.post(
        "tag",
    ) { setBody(query) }.body()

    suspend fun queryNovels(query: QueryDTO): BrowseNovelPageDTO = client.post(
        "vn",
    ) { setBody(query) }.body()
}
