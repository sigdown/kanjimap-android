package com.vb.kanjimap_android.core.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vb.kanjimap_android.core.database.entity.SavedWordEntity
import com.vb.kanjimap_android.core.database.entity.WordProgressEntity

data class SavedWordWithProgressRelation(
    @Embedded val word: SavedWordEntity,
    @Relation(
        parentColumn = "wordId",
        entityColumn = "wordId"
    )
    val progress: WordProgressEntity?
)
