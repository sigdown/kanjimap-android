package com.vb.kanjimap_android.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vb.kanjimap_android.core.database.entity.KanjiProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KanjiProgressDao {
    @Query("SELECT * FROM kanji_progress WHERE kanjiId = :kanjiId")
    fun getById(kanjiId: Long): Flow<KanjiProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: KanjiProgressEntity)
}
