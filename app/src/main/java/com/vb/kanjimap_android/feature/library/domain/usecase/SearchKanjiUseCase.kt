package com.vb.kanjimap_android.feature.library.domain.usecase

import com.vb.kanjimap_android.feature.library.domain.model.Kanji
import com.vb.kanjimap_android.feature.library.domain.repository.LibraryRepository
import javax.inject.Inject

class SearchKanjiUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(query: String): List<Kanji> = repository.searchKanji(query)
}
