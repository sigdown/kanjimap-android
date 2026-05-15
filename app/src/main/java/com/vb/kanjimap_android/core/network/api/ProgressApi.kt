package com.vb.kanjimap_android.core.network.api

import com.vb.kanjimap_android.core.network.dto.progress.KanjiProgressDto
import com.vb.kanjimap_android.core.network.dto.progress.UpdateKanjiProgressRequestDto
import com.vb.kanjimap_android.core.network.dto.progress.UpdateWordProgressRequestDto
import com.vb.kanjimap_android.core.network.dto.progress.WordProgressDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProgressApi {
    @GET("progress/words/{id}")
    suspend fun getWordProgress(@Path("id") id: Long): WordProgressDto

    @PUT("progress/words/{id}")
    suspend fun updateWordProgress(
        @Path("id") id: Long,
        @Body body: UpdateWordProgressRequestDto
    ): WordProgressDto

    @GET("progress/kanji/{id}")
    suspend fun getKanjiProgress(@Path("id") id: Long): KanjiProgressDto

    @PUT("progress/kanji/{id}")
    suspend fun updateKanjiProgress(
        @Path("id") id: Long,
        @Body body: UpdateKanjiProgressRequestDto
    ): KanjiProgressDto
}
