package com.vb.kanjimap_android.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vb.kanjimap_android.core.database.entity.SavedBlockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedBlockDao {
    @Query("SELECT * FROM saved_blocks ORDER BY savedAt DESC")
    fun getAll(): Flow<List<SavedBlockEntity>>

    @Query("SELECT * FROM saved_blocks WHERE learningBlockId = :learningBlockId")
    fun getById(learningBlockId: Long): Flow<SavedBlockEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedBlockEntity)

    @Query("DELETE FROM saved_blocks WHERE learningBlockId = :learningBlockId")
    suspend fun deleteById(learningBlockId: Long)
}
