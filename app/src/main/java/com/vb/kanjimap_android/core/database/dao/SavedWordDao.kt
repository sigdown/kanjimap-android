package com.vb.kanjimap_android.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vb.kanjimap_android.core.database.entity.SavedWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedWordDao {
    @Query("SELECT * FROM saved_words ORDER BY savedAt DESC")
    fun getAll(): Flow<List<SavedWordEntity>>

    @Query("SELECT * FROM saved_words WHERE wordId = :wordId")
    fun getById(wordId: Long): Flow<SavedWordEntity?>

    @Query("SELECT * FROM saved_words WHERE wordId = :wordId")
    suspend fun getByIdOnce(wordId: Long): SavedWordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedWordEntity)

    @Query("DELETE FROM saved_words WHERE wordId = :wordId")
    suspend fun deleteById(wordId: Long)
}
