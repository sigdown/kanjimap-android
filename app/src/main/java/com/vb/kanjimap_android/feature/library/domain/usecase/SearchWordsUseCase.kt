package com.vb.kanjimap_android.feature.library.domain.usecase

import com.vb.kanjimap_android.feature.library.domain.model.Word
import com.vb.kanjimap_android.feature.library.domain.repository.LibraryRepository
import javax.inject.Inject

class SearchWordsUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(query: String): List<Word> = repository.searchWords(query)
}
