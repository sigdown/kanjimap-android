package com.vb.kanjimap_android.core.common.time

import java.time.Instant

fun String?.toEpochMillisOrNull(): Long? =
    this?.let { value ->
        runCatching { Instant.parse(value).toEpochMilli() }.getOrNull()
    }

fun Long?.toInstantStringOrNull(): String? =
    this?.let { value ->
        runCatching { Instant.ofEpochMilli(value).toString() }.getOrNull()
    }
