package com.vb.kanjimap_android.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.sessionDataStore by preferencesDataStore(name = "kanjimap_datastore")

class SessionDataStore(
    private val context: Context
) {
    private object Keys {
        val accessToken = stringPreferencesKey("access_token")
        val userId = longPreferencesKey("user_id")
    }

    fun observeAccessToken(): Flow<String?> =
        context.sessionDataStore.data.map { prefs -> prefs[Keys.accessToken] }

    fun observeUserId(): Flow<Long?> =
        context.sessionDataStore.data.map { prefs -> prefs[Keys.userId] }

    suspend fun saveAccessToken(token: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.accessToken] = token
        }
    }

    suspend fun saveUserId(userId: Long) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.userId] = userId
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs.remove(Keys.accessToken)
            prefs.remove(Keys.userId)
        }
    }

    fun getAccessTokenSync(): String? = runBlocking {
        observeAccessToken().firstOrNull()
    }
}