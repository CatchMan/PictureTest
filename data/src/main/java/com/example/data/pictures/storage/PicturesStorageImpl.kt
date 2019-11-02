package com.example.data.pictures.storage

import com.example.data.AppDatabase
import io.reactivex.Single
import javax.inject.Inject



class PicturesStorageImpl @Inject constructor(database: AppDatabase) : PicturesStorage {

    private val todoDao = database.getTodoDao()


    override fun putPictures(pictures: List<StoragePictureInfo>): Single<String> {
        return if(todoDao.getAll().blockingGet().firstOrNull()?.id == pictures.first().id) {
            Single.fromCallable {
                todoDao.updateData(pictures)
                "OK"
            }
        } else {
            Single.fromCallable {
                todoDao.insertAll(pictures)
                "OK"
            }
        }

    }

    override fun getAllPictures(): Single<List<StoragePictureInfo>> = todoDao.getAll()

}