package com.vb.kanjimap_android.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vb.kanjimap_android.core.common.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.sessionDataStore by preferencesDataStore(name = Constants.SESSION_DATASTORE_NAME)

class SessionDataStore(
    private val context: Context
) {
    private object Keys {
        val accessToken = stringPreferencesKey("access_token")
        val userId = longPreferencesKey("user_id")
    }

    val accessToken: Flow<String?> =
        context.sessionDataStore.data.map { prefs -> prefs[Keys.accessToken] }

    val currentUserId: Flow<Long?> =
        context.sessionDataStore.data.map { prefs -> prefs[Keys.userId] }

    suspend fun setAccessToken(token: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.accessToken] = token
        }
    }

    suspend fun setCurrentUserId(userId: Long) {
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

    fun getAccessTokenBlocking(): String? = runBlocking {
        accessToken.firstOrNull()
    }
}
