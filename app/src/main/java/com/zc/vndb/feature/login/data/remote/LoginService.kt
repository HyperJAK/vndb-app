package com.zc.vndb.feature.login.data.remote

import com.zc.vndb.feature.login.data.dto.AuthInfoDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class LoginService(private val client: HttpClient) {

    /** Validates [token]; a 401 means VNDB rejected it. */
    suspend fun authInfo(token: String): AuthInfoDTO = client.get("authinfo") {
        header(HttpHeaders.Authorization, "Token $token")
    }.body()
}
