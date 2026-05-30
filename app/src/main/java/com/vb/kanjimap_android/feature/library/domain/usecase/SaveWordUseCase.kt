package com.vb.kanjimap_android.feature.library.domain.usecase

import com.vb.kanjimap_android.feature.library.domain.model.WordDetails
import com.vb.kanjimap_android.feature.library.domain.repository.LibraryRepository
import javax.inject.Inject

class SaveWordUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(details: WordDetails) {
        repository.saveWord(details)
    }
}
