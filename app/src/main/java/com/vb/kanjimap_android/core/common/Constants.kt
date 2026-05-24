package com.vb.kanjimap_android.core.common

import com.vb.kanjimap_android.BuildConfig


object Constants {
    const val BASE_URL = BuildConfig.HIDDEN_API_URL
    const val CONNECT_TIMEOUT_SECONDS = 15L
    const val READ_TIMEOUT_SECONDS = 15L
    const val WRITE_TIMEOUT_SECONDS = 15L
    const val SESSION_DATASTORE_NAME = "kanjimap_session"
    const val SETTINGS_DATASTORE_NAME = "kanjimap_settings"
    const val DATABASE_NAME = "kanjimap.db"
    const val AUTH_HEADER = "Authorization"
    const val BEARER_PREFIX = "Bearer"
    const val CONTENT_TYPE_JSON = "application/json"
}
