package com.example.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlin.collections.ArrayList




data class Picture(
    val id: Long,
    val author: String,
    val width: Long,
    val height: Long,
    val url: String,
    var downloadUrl: String
): Serializable
