package com.vb.kanjimap_android.feature.home.data.repository

import com.vb.kanjimap_android.core.network.api.AuthApi
import com.vb.kanjimap_android.core.network.api.LearningApi
import com.vb.kanjimap_android.core.network.api.ReviewApi
import com.vb.kanjimap_android.feature.home.domain.model.HomeBlockPreview
import com.vb.kanjimap_android.feature.home.domain.model.HomeSummary
import com.vb.kanjimap_android.feature.home.domain.repository.HomeRepository
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val reviewApi: ReviewApi,
    private val learningApi: LearningApi
) : HomeRepository {

    override suspend fun getHomeSummary(): HomeSummary = coroutineScope {
        val userDeferred = async { authApi.getCurrentUser() }
        val wordsDeferred = async { reviewApi.getReviewWords() }
        val kanjiDeferred = async { reviewApi.getReviewKanji() }
        val blocksDeferred = async { learningApi.getLearningBlocks() }

        val user = userDeferred.await()
        val reviewWordsCount = wordsDeferred.await().size
        val reviewKanjiCount = kanjiDeferred.await().size
        val blocksPreview = blocksDeferred.await()
            .sortedBy { it.orderIndex }
            .take(3)
            .map {
                HomeBlockPreview(
                    blockId = it.learningBlockId,
                    title = it.title,
                    blockType = it.blockType
                )
            }

        HomeSummary(
            username = user.username,
            reviewWordsCount = reviewWordsCount,
            reviewKanjiCount = reviewKanjiCount,
            totalReviewCount = reviewWordsCount + reviewKanjiCount,
            blocksPreview = blocksPreview
        )
    }
}
