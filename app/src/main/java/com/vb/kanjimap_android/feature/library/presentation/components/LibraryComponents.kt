package com.vb.kanjimap_android.feature.library.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.vb.kanjimap_android.core.ui.components.MetaText
import com.vb.kanjimap_android.core.ui.components.SectionCard
import com.vb.kanjimap_android.core.ui.components.SectionTitleText
import com.vb.kanjimap_android.core.ui.theme.CoreSpacing
import com.vb.kanjimap_android.core.ui.theme.Dimens
import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.model.Word

@Composable
fun LibrarySearchField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        label = { Text(label) },
        trailingIcon = {
            IconButton(
                onClick = onSearch,
                enabled = enabled
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Поиск"
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() })
    )
}

@Composable
fun LibrarySectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SectionCard(title = title, modifier = modifier, content = content)
}

@Composable
fun LibraryEmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = Dimens.emptyStateVerticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)
        ) {
            SectionTitleText(text = title)
            MetaText(text = description)
        }
    }
}

@Composable
fun WordListItem(
    word: Word,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(Dimens.cardContentPadding),
            verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)
        ) {
            SectionTitleText(text = word.writingForm)
            MetaText(text = word.readingKana)
            WordMetaRow(
                jlptLevel = word.jlptLevel,
                topicName = word.topicName
            )
        }
    }
}

@Composable
fun KanjiListItem(
    kanji: Kanji,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier.padding(Dimens.cardContentPadding),
            horizontalArrangement = Arrangement.spacedBy(CoreSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = kanji.literal,
                style = MaterialTheme.typography.headlineMedium
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(CoreSpacing.xs)
            ) {
                Text(
                    text = buildString {
                        append("ID ")
                        append(kanji.kanjiId)
                        kanji.strokeCount?.let {
                            append(" • ")
                            append(it)
                            append(" черт")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                kanji.jlptLevel?.let { jlpt ->
                    Text(
                        text = jlpt,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
fun KanjisRow(
    kanjis: List<Kanji>,
    onKanjiClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
    ) {
        items(kanjis, key = { it.kanjiId }) { kanji ->
            AssistChip(
                onClick = { onKanjiClick(kanji.kanjiId) },
                label = { Text(kanji.literal) }
            )
        }
    }
}

@Composable
fun RelatedWordsColumn(
    words: List<Word>,
    onWordClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(CoreSpacing.sm)
    ) {
        words.forEach { word ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onWordClick(word.wordId) }
                    .padding(vertical = CoreSpacing.xs),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = word.writingForm,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = word.readingKana,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                word.jlptLevel?.let { level ->
                    Text(
                        text = level,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun WordMetaRow(
    jlptLevel: String?,
    topicName: String?
) {
    val meta = listOfNotNull(jlptLevel, topicName)
    if (meta.isEmpty()) return

    Text(
        text = meta.joinToString(" • "),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
