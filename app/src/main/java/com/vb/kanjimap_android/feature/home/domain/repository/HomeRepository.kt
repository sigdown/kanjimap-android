package com.vb.kanjimap_android.feature.home.domain.repository

import com.vb.kanjimap_android.feature.home.domain.model.HomeSummary

interface HomeRepository {
    suspend fun hasSavedSession(): Boolean
    suspend fun getHomeSummary(): HomeSummary
}
