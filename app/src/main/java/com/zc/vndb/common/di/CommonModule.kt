package com.zc.vndb.common.di

import com.zc.vndb.BuildConfig
import com.zc.vndb.common.data.network.appJson
import com.zc.vndb.common.data.network.createHttpClient
import com.zc.vndb.common.domain.util.DefaultDispatcherProvider
import com.zc.vndb.common.domain.util.DispatcherProvider
import io.ktor.client.HttpClient
import kotlin.time.Clock
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val commonModule = module {
    single<Json> { appJson }
    single<HttpClient> { createHttpClient(json = get(), enableLogging = BuildConfig.DEBUG) }
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single<Clock> { Clock.System }
}
