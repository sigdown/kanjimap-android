package com.vb.kanjimap_android.core.network.provider

fun interface AccessTokenProvider {
    fun getAccessToken(): String?
}
