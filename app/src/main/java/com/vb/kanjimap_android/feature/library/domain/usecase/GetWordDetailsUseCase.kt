package com.vb.kanjimap_android.feature.library.domain.usecase

import com.vb.kanjimap_android.feature.library.domain.model.WordDetails
import com.vb.kanjimap_android.feature.library.domain.repository.LibraryRepository
import javax.inject.Inject

class GetWordDetailsUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(id: Long): WordDetails = repository.getWordDetails(id)
}
