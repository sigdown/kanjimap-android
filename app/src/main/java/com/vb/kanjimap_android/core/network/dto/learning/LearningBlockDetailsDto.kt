package com.vb.kanjimap_android.core.network.dto.learning

import com.vb.kanjimap_android.core.network.dto.dictionary.KanjiSearchItemDto
import com.vb.kanjimap_android.core.network.dto.dictionary.WordSearchItemDto
import kotlinx.serialization.Serializable

@Serializable
data class LearningBlockDetailsDto(
    val block: LearningBlockDto,
    val words: List<WordSearchItemDto>,
    val kanjis: List<KanjiSearchItemDto>
)
