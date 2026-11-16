package com.zc.vndb.feature.detail.data.remote

import com.zc.vndb.common.data.dto.QueryDTO
import com.zc.vndb.feature.detail.data.dto.CharacterPageDTO
import com.zc.vndb.feature.detail.data.dto.DetailNovelPageDTO
import com.zc.vndb.feature.detail.data.dto.EditionPageDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class DetailService(private val client: HttpClient) {

    suspend fun queryNovel(query: QueryDTO): DetailNovelPageDTO = client.post(
        "vn",
    ) { setBody(query) }.body()

    suspend fun queryCharacters(query: QueryDTO): CharacterPageDTO = client.post(
        "character",
    ) { setBody(query) }.body()

    suspend fun queryEditions(query: QueryDTO): EditionPageDTO = client.post(
        "release",
    ) { setBody(query) }.body()
}
