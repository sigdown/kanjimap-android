package com.vb.kanjimap_android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_blocks")
data class SavedBlockEntity(
    @PrimaryKey val learningBlockId: Long,
    val title: String,
    val description: String? = null,
    val blockType: String,
    val orderIndex: Int,
    val savedAt: Long
)
