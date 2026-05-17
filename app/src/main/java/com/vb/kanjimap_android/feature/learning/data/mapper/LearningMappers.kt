package com.vb.kanjimap_android.feature.learning.data.mapper

import com.vb.kanjimap_android.core.network.dto.dictionary.KanjiSearchItemDto
import com.vb.kanjimap_android.core.network.dto.dictionary.WordSearchItemDto
import com.vb.kanjimap_android.core.network.dto.learning.LearningBlockDetailsDto
import com.vb.kanjimap_android.core.network.dto.learning.LearningBlockDto
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlock
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlockDetails
import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.Word

fun LearningBlockDto.toDomain(): LearningBlock = LearningBlock(
    learningBlockId = learningBlockId,
    title = title,
    description = description,
    blockType = blockType,
    orderIndex = orderIndex
)

fun WordSearchItemDto.toLearningWord(): Word = Word(
    wordId = wordId,
    writingForm = writingForm,
    readingKana = readingKana,
    jlptLevel = jlptLevel,
    topicName = topicName
)

fun KanjiSearchItemDto.toLearningKanji(): Kanji = Kanji(
    kanjiId = kanjiId,
    literal = literal,
    strokeCount = strokeCount,
    jlptLevel = jlptLevel
)

fun LearningBlockDetailsDto.toDomain(): LearningBlockDetails = LearningBlockDetails(
    block = block.toDomain(),
    words = words.map(WordSearchItemDto::toLearningWord),
    kanjis = kanjis.map(KanjiSearchItemDto::toLearningKanji)
)
