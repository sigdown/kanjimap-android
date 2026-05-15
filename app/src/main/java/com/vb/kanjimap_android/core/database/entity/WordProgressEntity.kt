package com.vb.kanjimap_android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_progress")
data class WordProgressEntity(
    @PrimaryKey val wordId: Long,
    val status: String,
    val correctNumber: Int,
    val wrongNumber: Int,
    val repetitionLevel: Int,
    val lastReviewAt: Long? = null,
    val nextReviewAt: Long? = null,
    val updatedAt: Long
)
