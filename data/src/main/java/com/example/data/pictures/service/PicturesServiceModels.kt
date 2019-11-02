package com.example.data.pictures.service

import com.google.gson.annotations.SerializedName



data class PictureInfo(
        val id: Long,
        val author: String,
        val width: Long,
        val height: Long,
        val url: String,
        @SerializedName("download_url")
        val downloadUrl: String
)
