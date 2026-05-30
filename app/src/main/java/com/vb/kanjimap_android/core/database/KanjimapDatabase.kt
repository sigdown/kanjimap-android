package com.vb.kanjimap_android.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vb.kanjimap_android.core.database.dao.KanjiProgressDao
import com.vb.kanjimap_android.core.database.dao.SavedKanjiDao
import com.vb.kanjimap_android.core.database.dao.SavedWordDao
import com.vb.kanjimap_android.core.database.dao.WordProgressDao
import com.vb.kanjimap_android.core.database.entity.KanjiProgressEntity
import com.vb.kanjimap_android.core.database.entity.SavedKanjiEntity
import com.vb.kanjimap_android.core.database.entity.SavedWordEntity
import com.vb.kanjimap_android.core.database.entity.WordProgressEntity

@Database(
    entities = [
        SavedWordEntity::class,
        SavedKanjiEntity::class,
        WordProgressEntity::class,
        KanjiProgressEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class KanjimapDatabase : RoomDatabase() {
    abstract fun savedWordDao(): SavedWordDao
    abstract fun savedKanjiDao(): SavedKanjiDao
    abstract fun wordProgressDao(): WordProgressDao
    abstract fun kanjiProgressDao(): KanjiProgressDao
}
