package com.vb.kanjimap_android.app.di

import android.content.Context
import androidx.room.Room
import com.vb.kanjimap_android.core.common.Constants
import com.vb.kanjimap_android.core.database.KanjimapDatabase
import com.vb.kanjimap_android.core.database.dao.KanjiProgressDao
import com.vb.kanjimap_android.core.database.dao.SavedBlockDao
import com.vb.kanjimap_android.core.database.dao.SavedKanjiDao
import com.vb.kanjimap_android.core.database.dao.SavedWordDao
import com.vb.kanjimap_android.core.database.dao.WordProgressDao

object DatabaseModule {
    fun provideDatabase(context: Context): KanjimapDatabase =
        Room.databaseBuilder(
            context,
            KanjimapDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()

    fun provideSavedWordDao(database: KanjimapDatabase): SavedWordDao = database.savedWordDao()
    fun provideSavedKanjiDao(database: KanjimapDatabase): SavedKanjiDao = database.savedKanjiDao()
    fun provideSavedBlockDao(database: KanjimapDatabase): SavedBlockDao = database.savedBlockDao()
    fun provideWordProgressDao(database: KanjimapDatabase): WordProgressDao = database.wordProgressDao()
    fun provideKanjiProgressDao(database: KanjimapDatabase): KanjiProgressDao = database.kanjiProgressDao()
}
