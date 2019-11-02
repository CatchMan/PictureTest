package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.pictures.storage.PicturesDao
import com.example.data.pictures.storage.StoragePictureInfo


@Database(entities = [(StoragePictureInfo::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTodoDao(): PicturesDao


}