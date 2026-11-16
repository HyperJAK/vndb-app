package com.zc.vndb.common.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.zc.vndb.common.domain.util.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import org.koin.core.annotation.Single

/** The signed-in account in the `session` DataStore, with the token encrypted by [cipher]. */
@Single
class SessionLocalDataSource(
    private val dataStore: DataStore<Preferences>,
    private val cipher: TokenCipher,
    private val dispatchers: DispatcherProvider,
) {

    /** Null for a guest, or when the stored token can no longer be decrypted. */
    val session: Flow<StoredSession?> = dataStore.data
        .catch { error -> if (error is IOException) emit(emptyPreferences()) else throw error }
        .map { prefs -> prefs.toStoredSession() }
        .flowOn(dispatchers.io)

    suspend fun token(): String? = session.first()?.token

    suspend fun save(session: StoredSession) {
        val encrypted = withContext(dispatchers.io) { cipher.encrypt(session.token) }
        dataStore.edit { prefs ->
            prefs[TOKEN] = encrypted
            prefs[USER_ID] = session.userId
            prefs[USERNAME] = session.username
            prefs[PERMISSIONS] = session.permissions
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    private fun Preferences.toStoredSession(): StoredSession? {
        val token = this[TOKEN]?.let(cipher::decrypt)
        val userId = this[USER_ID]
        val username = this[USERNAME]
        return if (token != null && userId != null && username != null) {
            StoredSession(token, userId, username, this[PERMISSIONS].orEmpty())
        } else {
            null
        }
    }

    private companion object {
        val TOKEN = stringPreferencesKey("token")
        val USER_ID = stringPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val PERMISSIONS = stringSetPreferencesKey("permissions")
    }
}
