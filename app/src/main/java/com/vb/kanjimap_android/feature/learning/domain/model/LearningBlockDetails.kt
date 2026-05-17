package com.vb.kanjimap_android.feature.learning.domain.model

import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.Word

data class LearningBlockDetails(
    val block: LearningBlock,
    val words: List<Word>,
    val kanjis: List<Kanji>
)
