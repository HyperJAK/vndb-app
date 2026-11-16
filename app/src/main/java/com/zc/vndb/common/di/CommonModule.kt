package com.zc.vndb.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.zc.vndb.BuildConfig
import com.zc.vndb.common.data.network.LogcatHttpLogger
import com.zc.vndb.common.data.network.appJson
import com.zc.vndb.common.data.network.createHttpClient
import io.ktor.client.HttpClient
import kotlin.time.Clock
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.zc.vndb.common")
class CommonModule {
    @Single
    fun json(): Json = appJson

    @Single
    fun httpClient(json: Json): HttpClient =
        createHttpClient(json = json, logger = if (BuildConfig.DEBUG) LogcatHttpLogger() else null)

    @Single
    fun clock(): Clock = Clock.System

    // Signed-in VNDB account, shared by the login and profile features.
    @Single
    fun sessionDataStore(context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create { context.preferencesDataStoreFile("session") }
}
