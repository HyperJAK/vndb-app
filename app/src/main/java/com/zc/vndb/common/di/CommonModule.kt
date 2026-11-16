package com.zc.vndb.common.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.zc.vndb.BuildConfig
import com.zc.vndb.common.data.local.KeystoreTokenCipher
import com.zc.vndb.common.data.local.SessionLocalDataSource
import com.zc.vndb.common.data.local.TokenCipher
import com.zc.vndb.common.data.network.LogcatHttpLogger
import com.zc.vndb.common.data.network.appJson
import com.zc.vndb.common.data.network.createHttpClient
import com.zc.vndb.common.data.repository.SessionRepositoryImpl
import com.zc.vndb.common.domain.repository.SessionRepository
import com.zc.vndb.common.domain.util.DefaultDispatcherProvider
import com.zc.vndb.common.domain.util.DispatcherProvider
import io.ktor.client.HttpClient
import kotlin.time.Clock
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    single<Json> { appJson }
    single<HttpClient> {
        createHttpClient(json = get(), logger = if (BuildConfig.DEBUG) LogcatHttpLogger() else null)
    }
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single<Clock> { Clock.System }

    // Signed-in VNDB account, shared by the login and profile features.
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create { androidContext().preferencesDataStoreFile("session") }
    }
    single<TokenCipher> { KeystoreTokenCipher() }
    singleOf(::SessionLocalDataSource)
    singleOf(::SessionRepositoryImpl) { bind<SessionRepository>() }
}
