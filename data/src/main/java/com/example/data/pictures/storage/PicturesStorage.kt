package com.example.data.pictures.storage

import io.reactivex.Single




interface PicturesStorage {



    fun putPictures(countries: List<StoragePictureInfo>): Single<String>
    fun getAllPictures(): Single<List<StoragePictureInfo>>


}