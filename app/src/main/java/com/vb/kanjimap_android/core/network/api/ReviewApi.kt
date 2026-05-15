package com.vb.kanjimap_android.core.network.api

import com.vb.kanjimap_android.core.network.dto.review.CheckKanjiAnswerRequestDto
import com.vb.kanjimap_android.core.network.dto.review.CheckWordAnswerRequestDto
import com.vb.kanjimap_android.core.network.dto.review.ReviewItemDto
import com.vb.kanjimap_android.core.network.dto.review.ReviewResultDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApi {
    @GET("review/words")
    suspend fun getReviewWords(): List<ReviewItemDto>

    @GET("review/kanji")
    suspend fun getReviewKanji(): List<ReviewItemDto>

    @POST("review/words/{id}/check")
    suspend fun checkWordAnswer(
        @Path("id") id: Long,
        @Body body: CheckWordAnswerRequestDto
    ): ReviewResultDto

    @POST("review/kanji/{id}/check")
    suspend fun checkKanjiAnswer(
        @Path("id") id: Long,
        @Body body: CheckKanjiAnswerRequestDto
    ): ReviewResultDto
}
