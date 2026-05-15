package com.vb.kanjimap_android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kanji_progress")
data class KanjiProgressEntity(
    @PrimaryKey val kanjiId: Long,
    val status: String,
    val correctNumber: Int,
    val wrongNumber: Int,
    val repetitionLevel: Int,
    val lastReviewAt: Long? = null,
    val nextReviewAt: Long? = null,
    val updatedAt: Long
)
