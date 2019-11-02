package com.example.data.pictures.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Single




//TODO implement BaseDao https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1

@Dao
interface PicturesDao {

    @Transaction
    fun updateData(users: List<StoragePictureInfo>){
        deleteAll()
        insertAll(users)
    }
    @Query("SELECT * FROM picture")
    fun getAll(): Single<List<StoragePictureInfo>>

    @Insert(onConflict = REPLACE)
    fun insertAll(users: List<StoragePictureInfo>)

    @Query("DELETE from picture")
    fun deleteAll()
}
