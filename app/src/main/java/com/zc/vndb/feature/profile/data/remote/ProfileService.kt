package com.zc.vndb.feature.profile.data.remote

import com.zc.vndb.feature.profile.data.dto.ListEntryPageDTO
import com.zc.vndb.feature.profile.data.dto.ListLabelsDTO
import com.zc.vndb.feature.profile.data.dto.ListQueryDTO
import com.zc.vndb.feature.profile.data.dto.UserDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import org.koin.core.annotation.Single

/** Account endpoints. A [token] is sent as `Authorization: Token …`, so private data is included. */
@Single
class ProfileService(private val client: HttpClient) {

    suspend fun user(userId: String): Map<String, UserDTO?> = client.get("user") {
        parameter("q", userId)
        parameter("fields", "lengthvotes,lengthvotes_sum")
    }.body()

    suspend fun labels(userId: String, token: String?): ListLabelsDTO = client.get("ulist_labels") {
        parameter("user", userId)
        parameter("fields", "count")
        authorize(token)
    }.body()

    suspend fun entries(query: ListQueryDTO, token: String?): ListEntryPageDTO = client.post(
        "ulist",
    ) {
        authorize(token)
        setBody(query)
    }.body()

    private fun HttpRequestBuilder.authorize(token: String?) {
        token?.let { header(HttpHeaders.Authorization, "Token $it") }
    }
}
