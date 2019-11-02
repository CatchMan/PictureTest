package com.example.data.pictures.storage

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "picture")
data class StoragePictureInfo(
    @PrimaryKey val id: Long,
    val author: String,
    val width: Long,
    val height: Long,
    val url: String,
    val downloadUrl: String
)
