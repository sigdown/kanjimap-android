package com.vb.kanjimap_android.core.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vb.kanjimap_android.core.database.entity.KanjiProgressEntity
import com.vb.kanjimap_android.core.database.entity.SavedKanjiEntity

data class SavedKanjiWithProgressRelation(
    @Embedded val kanji: SavedKanjiEntity,
    @Relation(
        parentColumn = "kanjiId",
        entityColumn = "kanjiId"
    )
    val progress: KanjiProgressEntity?
)
