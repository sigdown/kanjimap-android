package com.vb.kanjimap_android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_words")
data class SavedWordEntity(
    @PrimaryKey val wordId: Long,
    val writingForm: String,
    val readingKana: String,
    val jlptLevel: String? = null,
    val topicName: String? = null,
    val detailsJson: String? = null,
    val savedAt: Long
)
