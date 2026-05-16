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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideKanjimapDatabase(
        @ApplicationContext context: Context
    ): KanjimapDatabase = Room.databaseBuilder(
        context,
        KanjimapDatabase::class.java,
        Constants.DATABASE_NAME
    ).build()

    @Provides
    fun provideSavedWordDao(database: KanjimapDatabase): SavedWordDao = database.savedWordDao()

    @Provides
    fun provideSavedKanjiDao(database: KanjimapDatabase): SavedKanjiDao = database.savedKanjiDao()

    @Provides
    fun provideSavedBlockDao(database: KanjimapDatabase): SavedBlockDao = database.savedBlockDao()

    @Provides
    fun provideWordProgressDao(database: KanjimapDatabase): WordProgressDao =
        database.wordProgressDao()

    @Provides
    fun provideKanjiProgressDao(database: KanjimapDatabase): KanjiProgressDao =
        database.kanjiProgressDao()
}
