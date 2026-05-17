package com.vb.kanjimap_android.core.network.api

import com.vb.kanjimap_android.core.network.dto.dictionary.KanjiDetailsDto
import com.vb.kanjimap_android.core.network.dto.dictionary.KanjiSearchItemDto
import com.vb.kanjimap_android.core.network.dto.dictionary.WordDetailsDto
import com.vb.kanjimap_android.core.network.dto.dictionary.WordSearchItemDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DictionaryApi {
    @GET("words")
    suspend fun searchWords(@Query("query") query: String): List<WordSearchItemDto>

    @GET("words/{id}")
    suspend fun getWordDetails(@Path("id") id: Long): WordDetailsDto

    @GET("kanji")
    suspend fun searchKanji(@Query("query") query: String): List<KanjiSearchItemDto>

    @GET("kanji/{id}")
    suspend fun getKanjiDetails(@Path("id") id: Long): KanjiDetailsDto
}
