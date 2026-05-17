package com.vb.kanjimap_android.feature.learning.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.SecondaryButton
import com.vb.kanjimap_android.core.ui.components.SectionCard
import com.vb.kanjimap_android.core.ui.components.SectionTitleText
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.core.ui.theme.Dimens
import com.vb.kanjimap_android.feature.learning.domain.model.LearningBlock
import com.vb.kanjimap_android.feature.learning.domain.model.StudyCard
import com.vb.kanjimap_android.feature.learning.domain.model.StudyCardType
import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.Word

@Composable
fun LearningEmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = Dimens.emptyStateVerticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
        ) {
            SectionTitleText(
                text = title,
                textAlign = TextAlign.Center
            )
            MetaText(
                text = description,
                textAlign = TextAlign.Center
            )
            action?.invoke()
        }
    }
}

@Composable
fun LearningSectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SectionCard(title = title, modifier = modifier, content = content)
}

@Composable
fun LearningBlockListItem(
    block: LearningBlock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = Dimens.cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(Dimens.cardContentPadding),
            verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = block.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = block.blockType,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            block.description?.let {
                MetaText(text = it)
            }
            MetaText(text = "Блок #${block.orderIndex + 1}")
        }
    }
}

@Composable
fun StudyModeButtons(
    showWords: Boolean,
    showKanji: Boolean,
    showAll: Boolean,
    onWordsClick: () -> Unit,
    onKanjiClick: () -> Unit,
    onAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
    ) {
        if (showWords) {
            SecondaryButton(onClick = onWordsClick, modifier = Modifier.fillMaxWidth()) {
                Text("Изучать слова")
            }
        }
        if (showKanji) {
            SecondaryButton(onClick = onKanjiClick, modifier = Modifier.fillMaxWidth()) {
                Text("Изучать кандзи")
            }
        }
        if (showAll) {
            SecondaryButton(onClick = onAllClick, modifier = Modifier.fillMaxWidth()) {
                Text("Изучать всё")
            }
        }
    }
}

@Composable
fun WordsChips(
    words: List<Word>,
    modifier: Modifier = Modifier
) {
    ChipGrid(
        modifier = modifier,
        itemCount = words.size
    ) { index ->
        val word = words[index]
        AssistChip(
            onClick = {},
            label = { Text("${word.writingForm} • ${word.readingKana}") }
        )
    }
}

@Composable
fun KanjiChips(
    kanjis: List<Kanji>,
    modifier: Modifier = Modifier
) {
    ChipGrid(
        modifier = modifier,
        itemCount = kanjis.size
    ) { index ->
        val kanji = kanjis[index]
        AssistChip(
            onClick = {},
            label = { Text(kanji.literal) }
        )
    }
}

@Composable
fun StudyCardView(
    card: StudyCard,
    isAnswerVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Dimens.cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(Dimens.cardContentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.sectionSpacing)
        ) {
            Text(
                text = if (card.type == StudyCardType.WORD) "Слово" else "Кандзи",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = card.prompt,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            if (isAnswerVisible) {
                card.answerTitle?.takeIf { it != card.prompt }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                card.answerSubtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(CoreSpacing.xs))
                when (card.type) {
                    StudyCardType.WORD -> {
                        StudyMetaBlock(label = "Чтение", values = card.readings)
                        StudyMetaBlock(label = "Значения", values = card.meanings)
                        StudyMetaBlock(label = "Примеры", values = card.examples)
                        StudyMetaBlock(label = "Связанные кандзи", values = card.relatedKanjis)
                    }

                    StudyCardType.KANJI -> {
                        StudyMetaBlock(label = "On readings", values = card.onReadings)
                        StudyMetaBlock(label = "Kun readings", values = card.kunReadings)
                        StudyMetaBlock(label = "Nanori", values = card.nanoriReadings)
                        StudyMetaBlock(label = "Значения", values = card.meanings)
                        StudyMetaBlock(label = "Слова с этим кандзи", values = card.relatedWords)
                        StudyMetaBlock(label = "Примечания", values = card.examples)
                    }
                }
            } else {
                Text(
                    text = "Нажмите «Показать ответ», чтобы раскрыть карточку.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun StudyMetaBlock(
    label: String,
    values: List<String>
) {
    if (values.isEmpty()) return

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        values.forEach { value ->
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ChipGrid(
    itemCount: Int,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    itemContent: @Composable (Int) -> Unit
) {
    if (itemCount == 0) return

    val rows = (0 until itemCount).chunked(columns)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
            ) {
                rowItems.forEach { index ->
                    Box(modifier = Modifier.weight(1f)) {
                        itemContent(index)
                    }
                }
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
