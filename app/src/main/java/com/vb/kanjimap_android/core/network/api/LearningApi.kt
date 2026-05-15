package com.vb.kanjimap_android.core.network.api

import com.vb.kanjimap_android.core.network.dto.learning.LearningBlockDetailsDto
import com.vb.kanjimap_android.core.network.dto.learning.LearningBlockDto
import retrofit2.http.GET
import retrofit2.http.Path

interface LearningApi {
    @GET("learning/blocks")
    suspend fun getLearningBlocks(): List<LearningBlockDto>

    @GET("learning/blocks/{id}")
    suspend fun getLearningBlockDetails(@Path("id") id: Long): LearningBlockDetailsDto
}
