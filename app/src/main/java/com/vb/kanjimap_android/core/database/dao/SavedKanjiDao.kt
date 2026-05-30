package com.vb.kanjimap_android.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vb.kanjimap_android.core.database.entity.SavedKanjiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedKanjiDao {
    @Query("SELECT * FROM saved_kanji ORDER BY savedAt DESC")
    fun getAll(): Flow<List<SavedKanjiEntity>>

    @Query("SELECT * FROM saved_kanji WHERE kanjiId = :kanjiId")
    fun getById(kanjiId: Long): Flow<SavedKanjiEntity?>

    @Query("SELECT * FROM saved_kanji WHERE kanjiId = :kanjiId")
    suspend fun getByIdOnce(kanjiId: Long): SavedKanjiEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedKanjiEntity)

    @Query("DELETE FROM saved_kanji WHERE kanjiId = :kanjiId")
    suspend fun deleteById(kanjiId: Long)
}
