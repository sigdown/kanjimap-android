package com.vb.kanjimap_android.app.di

import android.content.Context
import androidx.room.migration.Migration
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vb.kanjimap_android.core.common.Constants
import com.vb.kanjimap_android.core.database.KanjimapDatabase
import com.vb.kanjimap_android.core.database.dao.KanjiProgressDao
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

    private val migration1To2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE saved_words ADD COLUMN detailsJson TEXT")
            db.execSQL("ALTER TABLE saved_kanji ADD COLUMN detailsJson TEXT")
        }
    }

    private val migration2To3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS saved_blocks")
        }
    }

    @Provides
    @Singleton
    fun provideKanjimapDatabase(
        @ApplicationContext context: Context
    ): KanjimapDatabase = Room.databaseBuilder(
        context,
        KanjimapDatabase::class.java,
        Constants.DATABASE_NAME
    ).addMigrations(migration1To2, migration2To3).build()

    @Provides
    fun provideSavedWordDao(database: KanjimapDatabase): SavedWordDao = database.savedWordDao()

    @Provides
    fun provideSavedKanjiDao(database: KanjimapDatabase): SavedKanjiDao = database.savedKanjiDao()

    @Provides
    fun provideWordProgressDao(database: KanjimapDatabase): WordProgressDao =
        database.wordProgressDao()

    @Provides
    fun provideKanjiProgressDao(database: KanjimapDatabase): KanjiProgressDao =
        database.kanjiProgressDao()
}
