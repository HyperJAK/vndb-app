package com.zc.vndb.feature.release.data.remote

import com.zc.vndb.common.data.dto.QueryDTO
import com.zc.vndb.feature.release.data.dto.ReleaseDetailPageDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Single

@Single
class ReleaseService(private val client: HttpClient) {

    suspend fun queryRelease(query: QueryDTO): ReleaseDetailPageDTO = client.post(
        "release",
    ) { setBody(query) }.body()
}
