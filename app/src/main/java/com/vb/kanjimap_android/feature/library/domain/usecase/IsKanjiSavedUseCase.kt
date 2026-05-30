package com.vb.kanjimap_android.feature.library.domain.usecase

import com.vb.kanjimap_android.feature.library.domain.repository.LibraryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class IsKanjiSavedUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    operator fun invoke(kanjiId: Long): Flow<Boolean> = repository.isKanjiSaved(kanjiId)
}
