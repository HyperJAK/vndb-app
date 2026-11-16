package com.zc.vndb.feature.character.data.remote

import com.zc.vndb.common.data.dto.QueryDTO
import com.zc.vndb.feature.character.data.dto.CharacterNovelVoicesPageDTO
import com.zc.vndb.feature.character.data.dto.CharacterProfilePageDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class CharacterService(private val client: HttpClient) {

    suspend fun queryCharacter(query: QueryDTO): CharacterProfilePageDTO =
        client.post("character") { setBody(query) }.body()

    suspend fun queryNovelVoices(query: QueryDTO): CharacterNovelVoicesPageDTO =
        client.post("vn") { setBody(query) }.body()
}
