package com.zc.vndb.common.data.local

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.zc.vndb.common.domain.util.DispatcherProvider
import java.io.File
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

/** A real Preferences DataStore in [file], on the test scheduler. */
fun TestScope.sessionStore(file: File): SessionLocalDataSource {
    val dispatcher = StandardTestDispatcher(testScheduler)
    val dispatchers = object : DispatcherProvider {
        override val main: CoroutineDispatcher = dispatcher
        override val io: CoroutineDispatcher = dispatcher
        override val default: CoroutineDispatcher = dispatcher
    }
    val dataStore = PreferenceDataStoreFactory.create(scope = backgroundScope) { file }
    return SessionLocalDataSource(dataStore, FakeTokenCipher(), dispatchers)
}
