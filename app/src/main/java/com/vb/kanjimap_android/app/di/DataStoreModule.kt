package com.vb.kanjimap_android.app.di

import android.content.Context
import com.vb.kanjimap_android.core.datastore.SessionDataStore
import com.vb.kanjimap_android.core.datastore.SettingsDataStore

object DataStoreModule {
    fun provideSessionDataStore(context: Context): SessionDataStore = SessionDataStore(context)
    fun provideSettingsDataStore(context: Context): SettingsDataStore = SettingsDataStore(context)
}
