package com.vb.kanjimap_android.feature.library.domain.usecase

import com.vb.kanjimap_android.feature.library.domain.model.KanjiDetails
import com.vb.kanjimap_android.feature.library.domain.repository.LibraryRepository
import javax.inject.Inject

class GetKanjiDetailsUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(id: Long): KanjiDetails = repository.getKanjiDetails(id)
}
