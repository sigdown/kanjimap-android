package com.vb.kanjimap_android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_kanji")
data class SavedKanjiEntity(
    @PrimaryKey val kanjiId: Long,
    val literal: String,
    val strokeCount: Int? = null,
    val jlptLevel: String? = null,
    val savedAt: Long
)
