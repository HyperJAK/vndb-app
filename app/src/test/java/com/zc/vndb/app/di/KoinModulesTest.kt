package com.zc.vndb.app.di

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import kotlin.test.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class KoinModulesTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `all modules resolve`() {
        AppModule().module().verify(
            extraTypes = listOf(
                HttpClient::class,
                HttpClientEngine::class,
                HttpClientConfig::class,
            ),
        )
    }
}
